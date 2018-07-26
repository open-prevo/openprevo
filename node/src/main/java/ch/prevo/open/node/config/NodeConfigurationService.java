/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 *
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.node.config;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import static ch.prevo.open.encrypted.services.DataEncryptionService.ASYMMETRIC_TRANSFORMATION_ALGORITHM;


@Service
public class NodeConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeConfigurationService.class);

    private final ResourceLoader loader;
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Value("${open.prevo.node.config.file}")
    private String configFile;

    private final Map<String, PublicKey> allRetirementFundsPublicKeys = new HashMap<>();
    private final Map<String, PrivateKey> ownRetirementFundsPrivateKeys = new HashMap<>();

    @Inject
    public NodeConfigurationService(ResourceLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    public void init() {
        try {
            final Resource resource = loader.getResource(configFile);
            NodeConfigurationRawData rawConfig = mapper.readValue(resource.getInputStream(), new TypeReference<NodeConfigurationRawData>() {
            });
            rawConfig.otherRetirementFunds.forEach((uid, publicKeyString) -> allRetirementFundsPublicKeys.put(uid, convertPublicKey(uid, publicKeyString)));
            rawConfig.ownRetirementFunds.forEach((uid, publicPrivateKeyStrings) -> allRetirementFundsPublicKeys.put(uid, convertPublicKey(uid, publicPrivateKeyStrings)));
            rawConfig.ownRetirementFunds.forEach((uid, publicPrivateKeyStrings) -> ownRetirementFundsPrivateKeys.put(uid, convertPrivateKey(uid, publicPrivateKeyStrings)));
            LOGGER.info("Node configuration with encryption keys ok");
        } catch (IOException e) {
            LOGGER.warn("Unable to read bootstrap-data from " + configFile, e);
        }
    }

    public PrivateKey getPrivateKey(String uid) {
        return ownRetirementFundsPrivateKeys.get(uid);
    }

    public PublicKey getPublicKey(String uid) {
        return allRetirementFundsPublicKeys.get(uid);
    }

    private PublicKey convertPublicKey(String uid, PublicKeyString publicKey) {
        if (publicKey == null) {
            throw new IllegalStateException("Configuration error: cannot locate public key for pension fund " + uid);
        }
        try {
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.base64PublicKey)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error: cannot read private key", e);
        }
    }

    private PrivateKey convertPrivateKey(String uid, PublicPrivateKeyStrings publicPrivateKeyStrings) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(publicPrivateKeyStrings.base64PrivateKey));
            return KeyFactory.getInstance(ASYMMETRIC_TRANSFORMATION_ALGORITHM).generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new IllegalStateException("Configuration error, cannot read private key for pension fund " + uid, e);
        }

    }

    private static class NodeConfigurationRawData {
        public Map<String, PublicPrivateKeyStrings> ownRetirementFunds = new HashMap<>();
        public Map<String, PublicKeyString> otherRetirementFunds = new HashMap<>();
    }

    private static class PublicPrivateKeyStrings extends PublicKeyString {
        public String base64PrivateKey;
    }

    private static class PublicKeyString {
        public String base64PublicKey;
    }

}
