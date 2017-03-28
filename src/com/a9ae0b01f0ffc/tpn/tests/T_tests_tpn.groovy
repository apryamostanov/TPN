package com.a9ae0b01f0ffc.tpn.tests

import com.a9ae0b01f0ffc.middleware.implementation.T_middleware_sender
import com.a9ae0b01f0ffc.tpn.implementation.T_round_robin
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_http_message
import com.a9ae0b01f0ffc.tpn.main.T_main
import com.a9ae0b01f0ffc.tpn.main.T_tpn_base_5_context
import org.junit.Test

class T_tests_tpn {

    final String PC_CONF_FILE_NAME = "./src/com/a9ae0b01f0ffc/tpn/conf/commons.conf"

    @Test
    void test_001() {
        T_tpn_s.x().init_custom(PC_CONF_FILE_NAME)
        System.out.println(T_s.c().GC_MESSAGE_FORMAT_TOKEN_TRACE)
        String l_output_data = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tem=\"http://tempuri.org/\" xmlns:wdn=\"http://schemas.datacontract.org/2004/07/WDNotifyServices.DTO\">\n" +
                "   <soap:Header/>\n" +
                "   <soap:Body>\n" +
                "      <tem:TranHistNotification>\n" +
                "         <!--Optional:-->\n" +
                "         <tem:TranHistNotificationRequest>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:AvailableBalance>?</wdn:AvailableBalance>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:CurrentBalance>?</wdn:CurrentBalance>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:LastCreditAmount>?</wdn:LastCreditAmount>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:LastCreditDate>?</wdn:LastCreditDate>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:LastTransactionDate>?</wdn:LastTransactionDate>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:TransactionHistoryList>\n" +
                "               <!--Zero or more repetitions:-->\n" +
                "               <wdn:TransactionsHistory>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:AccountNumber>?</wdn:AccountNumber>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:BillingAmount>?</wdn:BillingAmount>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:BillingCurrencyCode>?</wdn:BillingCurrencyCode>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:CMTTranType>?</wdn:CMTTranType>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:CardAcceptorNameLocation>?</wdn:CardAcceptorNameLocation>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:CardMasking>?</wdn:CardMasking>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:CardNumber4Digits>?</wdn:CardNumber4Digits>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:IsDeclined>?</wdn:IsDeclined>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantCity>?</wdn:MerchantCity>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:PostTime>?</wdn:PostTime>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:PostingRef>?</wdn:PostingRef>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:ProductBin>?</wdn:ProductBin>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:ProxyNumber>?</wdn:ProxyNumber>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:SpendCategory>?</wdn:SpendCategory>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TranId>?</wdn:TranId>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TranTime>?</wdn:TranTime>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TransactionAmount>?</wdn:TransactionAmount>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TransactionCurrencyCode>?</wdn:TransactionCurrencyCode>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TransactionDescription>?</wdn:TransactionDescription>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TransactionType>?</wdn:TransactionType>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TxnSource>?</wdn:TxnSource>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TypeOfTran>?</wdn:TypeOfTran>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MCC>?</wdn:MCC>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantName>?</wdn:MerchantName>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantCountry>?</wdn:MerchantCountry>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantPostalCode>?</wdn:MerchantPostalCode>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantAddress>?</wdn:MerchantAddress>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:SettleAmount>?</wdn:SettleAmount>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:SettleCurrency>?</wdn:SettleCurrency>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TerminalID>?</wdn:TerminalID>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TerminalType>?</wdn:TerminalType>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:TraceNum>?</wdn:TraceNum>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:PostedAmount>?</wdn:PostedAmount>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:PostedCurrency>?</wdn:PostedCurrency>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:MerchantId>?</wdn:MerchantId>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:RRN>?</wdn:RRN>\n" +
                "                  <!--Optional:-->\n" +
                "                  <wdn:AcquirerCountry>?</wdn:AcquirerCountry>\n" +
                "               </wdn:TransactionsHistory>\n" +
                "            </wdn:TransactionHistoryList>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:UniqueID>?</wdn:UniqueID>\n" +
                "            <!--Optional:-->\n" +
                "            <wdn:UniqueIDflag>?</wdn:UniqueIDflag>\n" +
                "         </tem:TranHistNotificationRequest>\n" +
                "      </tem:TranHistNotification>\n" +
                "   </soap:Body>\n" +
                "</soap:Envelope>"
        T_middleware_sender.send_http_request(new T_tpn_http_message(l_output_data))
    }

    @Test
    void test_002() {
        T_main.main(PC_CONF_FILE_NAME)
    }

    @Test
    void test_003() {
        T_tpn_base_5_context.init_custom(PC_CONF_FILE_NAME)
        ArrayList<String> q = new ArrayList<String>()
        q.add("w")
        q.add("e")
        q.add("r")
        T_round_robin<String> z = new T_round_robin<String>(q)
        while (true) {
            System.out.println(z.iterator().next())
            Thread.sleep(1000)
        }
    }

}
