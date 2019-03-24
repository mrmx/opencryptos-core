package org.opencryptos.core.coins;

import org.opencryptos.core.coins.families.BitFamily;

/**
 * @author John L. Jegutanis
 */
public class LitecoinTest extends BitFamily {
    private LitecoinTest() {
        id = "litecoin.test";
        segwitAddressHrp = "tltc1";
        addressHeader = 111;
        p2shHeader = 196;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 239;

        name = "Litecoin Test";
        symbol = "LTCt";
        uriScheme = "litecoin";
        bip44Index = 1;
        unitExponent = 8;
        maxCoins = 84000000L;
        feeValue = value(100000);
        minNonDust = value(1000); // 0.00001 LTC mininput
        softDustLimit = value(100000); // 0.001 LTC
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
        signedMessageHeader = toBytes("Litecoin Signed Message:\n");
        bip32HeaderP2PKHpub = 0x0436f6e1; // The 4 byte header that serializes in base58 to "ttub".
        bip32HeaderP2PKHpriv = 0x0436ef7d; // The 4 byte header that serializes in base58 to "ttpv"
    }

    private static LitecoinTest instance = new LitecoinTest();
    public static synchronized CoinType get() {
        return instance;
    }
}
