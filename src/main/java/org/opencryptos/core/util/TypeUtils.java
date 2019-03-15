package org.opencryptos.core.util;

import org.opencryptos.core.coins.CoinType;
import org.opencryptos.core.coins.ValueType;
import org.opencryptos.core.wallet.AbstractAddress;

/**
 * @author John L. Jegutanis
 */
public class TypeUtils {

    public static boolean is(CoinType myType, ValueType otherType) {
        return otherType != null && myType.equals(otherType);
    }

    public static boolean is(CoinType myType, AbstractAddress address) {
        return address != null && myType.equals(address.getType());
    }
}
