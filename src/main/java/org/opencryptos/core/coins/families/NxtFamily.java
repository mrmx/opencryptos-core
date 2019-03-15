package org.opencryptos.core.coins.families;

import org.opencryptos.core.coins.CoinType;
import org.opencryptos.core.exceptions.AddressMalformedException;
import org.opencryptos.core.messages.MessageFactory;
import org.opencryptos.core.wallet.AbstractAddress;
import org.opencryptos.core.wallet.families.nxt.NxtAddress;
import org.opencryptos.core.wallet.families.nxt.NxtTxMessage;

import javax.annotation.Nullable;


/**
 * @author John L. Jegutanis
 *
 * Coins that belong to this family are: NXT, Burst, etc
 */
public abstract class NxtFamily extends CoinType {
    public static final short DEFAULT_DEADLINE = 1440;

    {
        family = Families.NXT;
    }

    @Override
    public AbstractAddress newAddress(String addressStr) throws AddressMalformedException {
        return NxtAddress.fromString(this, addressStr);
    }

    @Override
    public boolean canHandleMessages() {
        return true;
    }

    @Override
    @Nullable
    public MessageFactory getMessagesFactory() {
        return NxtTxMessage.getFactory();
    }
}
