package ch.prevo.open.hub;

import ch.prevo.open.encrypted.model.InsurantInformation;

public class Match {
    private final InsurantInformation exit;
    private final InsurantInformation entry;

    public Match(InsurantInformation exit, InsurantInformation entry) {
        this.exit = exit;
        this.entry = entry;
    }

    public InsurantInformation getExit() {
        return exit;
    }

    public InsurantInformation getEntry() {
        return entry;
    }
}
