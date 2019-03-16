package org.opencryptos.core.coins;

import org.opencryptos.core.coins.families.BitFamily;

/**
 * @author John L. Jegutanis
 */
public class LitecoinMain extends BitFamily {
    private LitecoinMain() {
        id = "litecoin.main";

        addressHeader = 0x30;//48
        p2shHeader = 0x32; //50
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 176;

        name = "Litecoin";
        symbol = "LTC";
        uriScheme = "litecoin";
        bip44Index = 2;
        unitExponent = 8;
        feeValue = value(100000);
        minNonDust = value(1000); // 0.00001 LTC mininput
        softDustLimit = value(100000); // 0.001 LTC
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
        signedMessageHeader = toBytes("Litecoin Signed Message:\n");
        bip32HeaderP2PKHpub = 0x019da462; // The 4 byte header that serializes in base58 to "Ltub".
        bip32HeaderP2PKHpriv = 0x019d9cfe; // The 4 byte header that serializes in base58 to "Ltpv"
        bip32HeaderP2WPKHpub = 0x01b26ef6; // The 4 byte header that serializes in base58 to "Mtub".
        bip32HeaderP2WPKHpriv = 0x01b26792; // The 4 byte header that serializes in base58 to "Mtpv"        
    }

    private static LitecoinMain instance = new LitecoinMain();
    public static synchronized CoinType get() {
        return instance;
    }
}
