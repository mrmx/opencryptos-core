package org.opencryptos.core.wallet.families.bitcoin;

import org.opencryptos.core.coins.CoinType;
import org.opencryptos.core.exceptions.AddressMalformedException;
import org.opencryptos.core.wallet.AbstractAddress;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.script.Script;

import java.nio.ByteBuffer;
import org.bitcoinj.core.AddressFormatException.WrongNetwork;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;

/**
 * @author John L. Jegutanis
 */
public class BitAddress extends Address implements AbstractAddress {

    private Address address;

    BitAddress(Address address, NetworkParameters params, byte[] bytes) {
        super(params, bytes);
        this.address = address;
    }

    BitAddress(Address address) throws WrongNetwork {
        this(address, address.getParameters(), address.getHash());
    }

    BitAddress(CoinType type, byte[] hash160) {
        this(LegacyAddress.fromPubKeyHash(type, hash160), type, hash160);
    }

    BitAddress(CoinType type, int version, byte[] hash160) throws WrongNetwork {
        super(type, hash160);
        if (version == type.getAddressHeader()) {
            address = LegacyAddress.fromPubKeyHash(type, hash160);
        } else if (version == type.getP2SHHeader()) {
            address = LegacyAddress.fromScriptHash(type, hash160);
        } else {
            throw new WrongNetwork(version);
        }
    }

    BitAddress(CoinType type, String address) throws AddressFormatException {
        this(Address.fromString(type, address));
    }

    public static BitAddress from(CoinType type, String address) throws AddressMalformedException {
        try {
            return new BitAddress(type, address);
        } catch (AddressFormatException e) {
            throw new AddressMalformedException(e);
        }
    }

    public static BitAddress from(CoinType type, int version, byte[] hash160)
            throws AddressMalformedException {
        try {
            return new BitAddress(type, version, hash160);
        } catch (WrongNetwork e) {
            throw new AddressMalformedException(e);
        }
    }

    public static BitAddress from(CoinType type, byte[] publicKeyHash160)
            throws AddressMalformedException {
        try {
            return new BitAddress(type, type.getAddressHeader(), publicKeyHash160);
        } catch (WrongNetwork e) {
            throw new AddressMalformedException(e);
        }
    }

    public static BitAddress from(CoinType type, Script script) throws AddressMalformedException {
        try {
            return new BitAddress(script.getToAddress(type));
        } catch (WrongNetwork e) {
            throw new AddressMalformedException(e);
        }
    }

    public static BitAddress from(CoinType type, ECKey key) {
        return new BitAddress(type, key.getPubKeyHash());
    }

    public static BitAddress from(AbstractAddress address) throws AddressMalformedException {
        try {
            if (address instanceof BitAddress) {
                return (BitAddress) address;
            } else if (address instanceof Address) {
                return new BitAddress((Address) address);
            } else {
                return new BitAddress(address.getType(), address.toString());
            }
        } catch (AddressFormatException e) {
            throw new AddressMalformedException(e);
        }
    }

    public static BitAddress from(Address address) throws AddressMalformedException {
        try {
            return new BitAddress(address);
        } catch (WrongNetwork e) {
            throw new AddressMalformedException(e);
        }
    }

    public Address getWrappedAddress() {
        return address;
    }

    /**
     * Get the type of output script that will be used for sending to the
     * address.
     *
     * @return type of output script
     */
    @Override
    public Script.ScriptType getOutputScriptType() {
        return address.getOutputScriptType();
    }

    @Override
    public byte[] getHash() {
        return address.getHash();
    }

    @Override
    public CoinType getType() {
        return (CoinType) getParameters();
    }

    @Override
    public long getId() {
        return ByteBuffer.wrap(getHash()).getLong();
    }

    @Override
    public String toString() {
        return address.toString();
    }
    
    
}
