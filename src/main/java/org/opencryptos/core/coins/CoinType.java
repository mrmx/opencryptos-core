package org.opencryptos.core.coins;


import org.opencryptos.core.exceptions.AddressMalformedException;
import org.opencryptos.core.messages.MessageFactory;
import org.opencryptos.core.wallet.AbstractAddress;
import com.google.common.base.Charsets;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.HDUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import org.opencryptos.core.coins.families.Families;
import org.opencryptos.core.util.MonetaryFormat;
import org.bitcoinj.core.BitcoinSerializer;
import org.bitcoinj.core.Block;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;


/**
 * @author John L. Jegutanis
 */
abstract public class CoinType extends NetworkParameters implements ValueType, Serializable {
    private static final long serialVersionUID = 1L;

    private static final String BIP_44_KEY_PATH = "44H/%dH/%dH";
    //Legacy
    protected int[] acceptableAddressCodes;
    
    protected Families family;

    protected String name;
    protected String symbol;
    protected Integer tokenId;
    protected String uriScheme;
    protected Integer bip44Index;
    protected Integer unitExponent;
    protected Integer transactionVersion;
    protected String addressPrefix;
    protected Long maxCoins;
    protected Value feeValue;
    protected Value minNonDust;
    protected Value softDustLimit;
    protected SoftDustPolicy softDustPolicy;
    protected FeePolicy feePolicy = FeePolicy.FEE_PER_KB;
    protected byte[] signedMessageHeader;

    private transient org.bitcoinj.utils.MonetaryFormat friendlyFormatCompat;
    private transient MonetaryFormat friendlyFormat;
    private transient MonetaryFormat plainFormat;
    private transient Value oneCoin;

    private static FeeProvider feeProvider = null;

    public int[] getAcceptableAddressCodes() {
        return acceptableAddressCodes;
    }
    
    @Override
    public String getName() {
        return checkNotNull(name, "A coin failed to set a name");
    }

    public boolean isTestnet() {
        return id.endsWith("test");
    }

    @Override
    public String getSymbol() {
        return checkNotNull(symbol, "A coin failed to set a symbol");
    }

    public String getUriScheme() {
        return checkNotNull(uriScheme, "A coin failed to set a URI scheme");
    }

    public int getBip44Index() {
        return checkNotNull(bip44Index, "A coin failed to set a BIP 44 index");
    }

    @Override
    public int getUnitExponent() {
        return checkNotNull(unitExponent, "A coin failed to set a unit exponent");
    }

    public Value getFeeValue() {
        if (feeProvider != null) {
            return feeProvider.getFeeValue(this);
        } else {
            return getDefaultFeeValue();
        }
    }

    public Value getDefaultFeeValue() {
        return checkNotNull(feeValue, "A coin failed to set a fee value");
    }

    @Override
    public Value getMinNonDust() {
        return checkNotNull(minNonDust, "A coin failed to set a minimum amount to be considered not dust");
    }

    public Value getSoftDustLimit() {
        return checkNotNull(softDustLimit, "A coin failed to set a soft dust limit");
    }

    public SoftDustPolicy getSoftDustPolicy() {
        return checkNotNull(softDustPolicy, "A coin failed to set a soft dust policy");
    }

    public FeePolicy getFeePolicy() {
        return checkNotNull(feePolicy, "A coin failed to set a fee policy");
    }

    public byte[] getSignedMessageHeader() {
        return checkNotNull(signedMessageHeader, "A coin failed to set signed message header bytes");
    }

    public boolean canSignVerifyMessages() {
        return signedMessageHeader != null;
    }

    public boolean canHandleMessages() {
        return getMessagesFactory() != null;
    }

    @Nullable
    public MessageFactory getMessagesFactory() {
        return null;
    }

    protected static byte[] toBytes(String str) {
        return str.getBytes(Charsets.UTF_8);
    }

    public List<ChildNumber> getBip44Path(int account) {
        String path = String.format(BIP_44_KEY_PATH, bip44Index, account);
        return HDUtils.parsePath(path);
    }

    /**
        Return an address prefix like NXT- or BURST-, otherwise and empty string
     */
    public String getAddressPrefix() {
        return checkNotNull(addressPrefix, "A coin failed to set the address prefix");
    }

    @Override
    public int getProtocolVersionNum(ProtocolVersion version) {
        return version.getBitcoinProtocolVersion();
    }

    @Override
    public BitcoinSerializer getSerializer(boolean parseRetain) {
        return null;
    }
    
    @Override
    public boolean hasMaxMoney() {
        return maxCoins != null && maxCoins > 0;
    }

    public Long getMaxCoins() {
        return maxCoins;
    }

    @Override
    public Coin getMaxMoney() {
        return oneCoin().toCoin().multiply(maxCoins);
    }

    @Override
    public Coin getMinNonDustOutput() {
        return minNonDust.toCoin();
    }

    @Override
    public void checkDifficultyTransitions(StoredBlock storedPrev, Block next, BlockStore blockStore) throws VerificationException, BlockStoreException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

    public abstract AbstractAddress newAddress(String addressStr) throws AddressMalformedException;

    /**
     * Returns a 1 coin of this type with the correct amount of units (satoshis)
     * Use {@link org.opencryptos.core.coins.CoinType}
     */
    @Deprecated
    public Coin getOneCoin() {
        BigInteger units = BigInteger.TEN.pow(getUnitExponent());
        return Coin.valueOf(units.longValue());
    }

    @Override
    public Value oneCoin() {
        if (oneCoin == null) {
            BigInteger units = BigInteger.TEN.pow(getUnitExponent());
            oneCoin = Value.valueOf(this, units.longValue());
        }
        return oneCoin;
    }

    @Override
    public Value value(String string) {
        return Value.parse(this, string);
    }

    @Override
    public Value value(Coin coin) {
        return Value.valueOf(this, coin);
    }

    @Override
    public Value value(long units) {
        return Value.valueOf(this, units);
    }

    @Override
    public String getPaymentProtocolId() {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public String toString() {
        return "Coin{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", bip44Index=" + bip44Index +
                '}';
    }
    
    @Override
    public org.bitcoinj.utils.MonetaryFormat getMonetaryFormat() {
        if(friendlyFormatCompat == null) {
            friendlyFormatCompat = new org.bitcoinj.utils.MonetaryFormat()
                    .shift(0).minDecimals(2).code(0, symbol).postfixCode();
            switch (unitExponent) {
                case 8:
                    friendlyFormat = friendlyFormat.optionalDecimals(2, 2, 2);
                    break;
                case 6:
                    friendlyFormat = friendlyFormat.optionalDecimals(2, 2);
                    break;
                case 4:
                    friendlyFormat = friendlyFormat.optionalDecimals(2);
                    break;
                default:
                    friendlyFormat = friendlyFormat.minDecimals(unitExponent);
            }
        }
        return friendlyFormatCompat;
    }

    @Override
    public MonetaryFormat getMonetaryFormatEx() {
        if (friendlyFormat == null) {
            friendlyFormat = new MonetaryFormat()
                    .shift(0).minDecimals(2).code(0, symbol).postfixCode();
            switch (unitExponent) {
                case 8:
                    friendlyFormat = friendlyFormat.optionalDecimals(2, 2, 2);
                    break;
                case 6:
                    friendlyFormat = friendlyFormat.optionalDecimals(2, 2);
                    break;
                case 4:
                    friendlyFormat = friendlyFormat.optionalDecimals(2);
                    break;
                default:
                    friendlyFormat = friendlyFormat.minDecimals(unitExponent);
            }
        }
        return friendlyFormat;
    }

    @Override
    public MonetaryFormat getPlainFormat() {
        if (plainFormat == null) {
            plainFormat = new MonetaryFormat().shift(0)
                    .minDecimals(0).repeatOptionalDecimals(1, unitExponent).noCode();
        }
        return plainFormat;
    }

    @Override
    public boolean equals(ValueType obj) {
        return super.equals(obj);
    }

    public static void setFeeProvider(FeeProvider feeProvider) {
        CoinType.feeProvider = feeProvider;
    }

    public interface FeeProvider {
        Value getFeeValue(CoinType type);
    }
}
