package org.opencryptos.core.util;

import static com.google.common.base.Preconditions.checkArgument;
import org.opencryptos.core.exceptions.AddressMalformedException;
import org.opencryptos.core.wallet.AbstractAddress;
import org.opencryptos.core.wallet.families.bitcoin.BitAddress;

import org.bitcoinj.script.Script;


/**
 * @author John L. Jegutanis
 */
public class BitAddressUtils {
    public static boolean isP2SHAddress(AbstractAddress address) {
        checkArgument(address instanceof BitAddress, "This address cannot be a P2SH address");
        return ((BitAddress) address).getOutputScriptType() == Script.ScriptType.P2SH;
    }

    public static byte[] getHash160(AbstractAddress address) {
        checkArgument(address instanceof BitAddress, "Cannot get hash160 from this address");
        return ((BitAddress) address).getWrappedAddress().getHash();
    }

    public static boolean producesAddress(Script script, AbstractAddress address) {
        try {
            return BitAddress.from(address.getType(), script).equals(address);
        } catch (AddressMalformedException e) {
            return false;
        }
    }
}
