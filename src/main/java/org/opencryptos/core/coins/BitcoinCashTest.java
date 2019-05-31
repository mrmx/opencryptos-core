package org.opencryptos.core.coins;

import org.opencryptos.core.coins.families.BitFamily;

/**
 *
 */
public class BitcoinCashTest extends BitFamily {

    private BitcoinCashTest() {
        id = "bitcoin_cash.test";
        addressHeader = 0;
        p2shHeader = 5;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader};
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 128;

        name = "Bitcoin Cash Test";
        symbol = "BCHt";
        uriScheme = "bitcoincash";
        bip44Index = 1;
        unitExponent = 8;
        maxCoins = 21000000L;
        feeValue = value(12000);
        minNonDust = value(5460);
        softDustLimit = value(1000000); // 0.01 BTC
        softDustPolicy = SoftDustPolicy.AT_LEAST_BASE_FEE_IF_SOFT_DUST_TXO_PRESENT;
        signedMessageHeader = toBytes("Bitcoin Signed Message:\n");
        bip32HeaderP2PKHpub = 0x04358394; // The 4 byte header that serializes in base58 to "xpriv".
        bip32HeaderP2PKHpriv = 0x043587cf; // The 4 byte header that serializes in base58 to "xpriv"             
    }

    private static BitcoinCashTest instance = new BitcoinCashTest();

    public static synchronized CoinType get() {
        return instance;
    }
}
