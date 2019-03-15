package org.opencryptos.core.coins.families;

import org.opencryptos.core.coins.CoinType;
import org.opencryptos.core.exceptions.AddressMalformedException;
import org.opencryptos.core.wallet.families.bitcoin.BitAddress;
import org.bitcoinj.core.BitcoinSerializer;


/**
 * @author John L. Jegutanis
 *
 * This is the classical Bitcoin family that includes Litecoin, Dogecoin, Dash, etc
 */
public abstract class BitFamily extends CoinType {
    {
        family = Families.BITCOIN;
    }

    @Override
    public BitAddress newAddress(String addressStr) throws AddressMalformedException {
        return BitAddress.from(this, addressStr);
    }
    
    @Override
    public BitcoinSerializer getSerializer(boolean parseRetain) {
        return new BitcoinSerializer(this, parseRetain);
    }
}
