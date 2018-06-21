# OpenPrevo

[![Build Status](https://travis-ci.org/open-prevo/openprevo.svg?branch=master)](https://travis-ci.org/open-prevo/openprevo)
[![Gitter](https://img.shields.io/gitter/room/open-prevo/Lobby.svg)](https://gitter.im/open-prevo/Lobby)

## Naming

An **open** [**prévo**yance professionnelle](https://www.bsv.admin.ch/bsv/en/home/social-insurance/bv/grundlagen-und-gesetze/grundlagen/sinn-und-zweck.html) platform supporting the [second pillar of the Swiss retirement provision](https://www.ch.ch/en/manage-retirement-provision/).

## Introduction [Slides](https://gitpitch.com/open-prevo/slides/intro)

### Switching Jobs
#### The problem

<img src="http://yuml.me/diagram/plain/activity/(start)->(new job),(new job)->|a|,|a|->(notify old employer)->(notify old VE)->(contact person),|a|->(notify new employer)->(notify new VE)->(contact person)->(notify old VE)-><c>[is valid]->(send money and document)->(end),<c>[invalid]->(notify old VE)"/>

#### The idea

<img src="http://yuml.me/diagram/plain/activity/(start)->(new job),(new job)->|a|,|a|->(notify old employer)->(notify old VE)->(use OpenPrevo),|a|->(notify new employer)->(notify new VE)->(use OpenPrevo)-><c>[new VE found]->(notify VEs, send money and document)->(end),<c>[no result]->(notify old VE)"/>

## [Architecture](doc/arc42/src/03_system_scope_and_context.adoc)

## [Glossar](doc/glossar.md)
