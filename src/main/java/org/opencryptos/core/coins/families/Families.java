package org.opencryptos.core.coins.families;

import org.opencryptos.core.coins.ValueType;

/**
 * @author John L. Jegutanis
 */
public enum Families {
    NXT("nxt"),
    FIAT("fiat"),
    // same as in org.bitcoinj.params.Networks
    BITCOIN("bitcoin"),
    NUBITS("nubits"),
    PEERCOIN("peercoin"),
    REDDCOIN("reddcoin"),
    VPNCOIN("vpncoin"),
    CLAMS("clams"),
    ;

    public final String family;

    Families(String family) {
        this.family = family;
    }

    @Override
    public String toString() {
        return family;
    }
}
