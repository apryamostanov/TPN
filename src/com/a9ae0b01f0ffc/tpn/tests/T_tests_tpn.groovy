package com.a9ae0b01f0ffc.tpn.tests

import com.a9ae0b01f0ffc.black_box.main.T_logging_base_6_util
import com.a9ae0b01f0ffc.commons.implementation.round_robin.T_round_robin
import com.a9ae0b01f0ffc.middleware.implementation.T_middleware_sender
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_http_message
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_conversion_module
import com.a9ae0b01f0ffc.tpn.main.T_main
import com.a9ae0b01f0ffc.tpn.main.T_tpn_base_5_context
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
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

    @Test
    void test_004() {
        T_tpn_base_5_context.init_custom(PC_CONF_FILE_NAME)
        T_tpn_http_message a = new T_tpn_http_message()
        String q = """<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:tem="http://tempuri.org/" xmlns:a="https://wp1.wirecard.com/TransactionNotification/Fleet">
 <soap:Header/>
 <soap:Body>
 <tem:TransactionNotificationRequest>
  <a:product>
    <a:productID>1530</a:productID>
    <a:productName>019 - GFS SG Virtual - SGD - 426437</a:productName>
    <a:programManager>GFS_SG</a:programManager>
    <a:productCategory />
    <a:subProductType />
  </a:product>
  <a:businessAccount>
    <a:sourceBusinessName>WS_SG_VIRTUAL_BA_SGD</a:sourceBusinessName>
    <a:sourceBusinessAccountNumber>1003000000000000011</a:sourceBusinessAccountNumber>
    <a:businessName>WS_SG_VIRTUAL_BA_SGD</a:businessName>
    <a:businessAccountNumber>1003000000000000011</a:businessAccountNumber>
  </a:businessAccount>
  <a:card>
    <a:sourceCardNumber>426437XXXXXX0013</a:sourceCardNumber>
    <a:sourceProxyNumber>1005</a:sourceProxyNumber>
    <a:cardNumber>426437XXXXXX2241</a:cardNumber>
    <a:proxyNumber>3458</a:proxyNumber>
    <a:cardSequenceNumber>1</a:cardSequenceNumber>
  </a:card>
  <a:account>
    <a:sourceAccountNumber>1003000000000000011</a:sourceAccountNumber>
    <a:sourceAccountCurrency>SGD</a:sourceAccountCurrency>
    <a:sourceAccountBalanceInt>914</a:sourceAccountBalanceInt>
    <a:sourceAccountBalanceDec>0</a:sourceAccountBalanceDec>
    <a:AccountNumber>1003000000000012594</a:AccountNumber>
    <a:AccountCurrency>SGD</a:AccountCurrency>
    <a:AccountBalanceInt>10556</a:AccountBalanceInt>
    <a:AccountBalanceDec>50</a:AccountBalanceDec>
  </a:account>
  <a:transaction>
    <a:transactionId>3915313</a:transactionId>
    <a:transactionCode>9951</a:transactionCode>
    <a:transactionDescription>WS_LOAD - Manual Card Load</a:transactionDescription>
    <a:transactionTimeStamp>29-Mar-2017 11:52:21 AM</a:transactionTimeStamp>
    <a:transactionLocalDate>29-Mar-2017</a:transactionLocalDate>
    <a:transactionLocalTime>11:52:21</a:transactionLocalTime>
    <a:transactionLifecycleState>O</a:transactionLifecycleState>
    <a:transactionOriginalId>0</a:transactionOriginalId>
    <a:messageTypeIdentifier />
    <a:transactionResponseCode />
    <a:debitCreditFlag>+</a:debitCreditFlag>
    <a:logicModule>37</a:logicModule>
    <a:transactionPostingDate>29-Mar-2017</a:transactionPostingDate>
    <a:transactionPostingTime>11:52:21</a:transactionPostingTime>
    <a:postingFlag>1</a:postingFlag>
    <a:postingReference>Transaction posted successfully</a:postingReference>
    <a:postingTransactionSource>CoreMoney</a:postingTransactionSource>
    <a:originalTransactionSource>CoreMoney</a:originalTransactionSource>
  </a:transaction>
  <a:network>
    <a:networkName>VS</a:networkName>
    <a:networkTransactionId>0</a:networkTransactionId>
    <a:networkProcessingCode>0</a:networkProcessingCode>
    <a:networkRRN>0</a:networkRRN>
    <a:networkSTAN>0</a:networkSTAN>
    <a:networkResponseCode />
  </a:network>
  <a:amounts>
    <a:sourcePostedCurrency>SGD</a:sourcePostedCurrency>
    <a:sourcePostedAmountInt>100</a:sourcePostedAmountInt>
    <a:sourcePostedAmountDec>0</a:sourcePostedAmountDec>
    <a:transactionCurrency>SGD</a:transactionCurrency>
    <a:transactionAmountInt>100</a:transactionAmountInt>
    <a:transactionAmountDec>0</a:transactionAmountDec>
    <a:billingCurrency>SGD</a:billingCurrency>
    <a:billingAmountInt>100</a:billingAmountInt>
    <a:billingAmountDec>0</a:billingAmountDec>
    <a:postedCurrency>SGD</a:postedCurrency>
    <a:postedAmountInt>100</a:postedAmountInt>
    <a:postedAmountDec>0</a:postedAmountDec>
    <a:additionalAmountCurrency />
    <a:additionalAmountInt>0</a:additionalAmountInt>
    <a:additionalAmountDec>0</a:additionalAmountDec>
  </a:amounts>
  <a:fees>
              <a:fee>
               <!--Optional:-->
               <a:feeName>?</a:feeName>
               <!--Optional:-->
               <a:feeAmountCurrency>?</a:feeAmountCurrency>
               <!--Optional:-->
               <a:feeAmountInt>?</a:feeAmountInt>
               <!--Optional:-->
               <a:feeAmountDec>?</a:feeAmountDec>
               <!--Optional:-->
               <a:feeTransactionId>?</a:feeTransactionId>
            </a:fee>
                        <a:fee>
               <!--Optional:-->
               <a:feeName>?</a:feeName>
               <!--Optional:-->
               <a:feeAmountCurrency>?</a:feeAmountCurrency>
               <!--Optional:-->
               <a:feeAmountInt>?</a:feeAmountInt>
               <!--Optional:-->
               <a:feeAmountDec>?</a:feeAmountDec>
               <!--Optional:-->
               <a:feeTransactionId>?</a:feeTransactionId>
            </a:fee>
  </a:fees>
  <a:pos_merchant>
    <a:acquirerId>0</a:acquirerId>
    <a:acquirerCountry />
    <a:acquirerInstitutionIDCode />
    <a:storeNumber />
    <a:terminalId />
    <a:terminalType />
    <a:terminalCategory>0</a:terminalCategory>
    <a:terminalEntry>0</a:terminalEntry>
    <a:cardAcceptorIDCode />
    <a:cardAcceptorterminalIDCode />
    <a:merchantId />
    <a:merchantCode />
    <a:merchantName />
    <a:merchantAddress />
    <a:merchantCity />
    <a:merchantCountry />
    <a:merchantType />
    <a:posEntryMode>0</a:posEntryMode>
    <a:posConditionCode>0</a:posConditionCode>
    <a:posPINCaptureCode />
    <a:nationalPOSGeographicData />
    <a:chipCondition>0</a:chipCondition>
    <a:chipTransactionFlag>0</a:chipTransactionFlag>
    <a:paymentIndicator />
    <a:cardholderId>0</a:cardholderId>
  </a:pos_merchant>
  <a:fleet_48_data />
  <a:fleet_104_data>
    <a:Quantity>0</a:Quantity>
    <a:Unit_Cost>0</a:Unit_Cost>
    <a:Gross_Fuel_Price>0</a:Gross_Fuel_Price>
    <a:Net_Fuel_Price>0</a:Net_Fuel_Price>
    <a:Gross_NonFuel_Price>0</a:Gross_NonFuel_Price>
    <a:Net_NonFuel_Price>0</a:Net_NonFuel_Price>
    <a:VAT_Rate>0</a:VAT_Rate>
    <a:Misc_FuelTax>0</a:Misc_FuelTax>
    <a:Misc_NonFuelTax>0</a:Misc_NonFuelTax>
    <a:Local_Tax>0</a:Local_Tax>
    <a:National_Tax>0</a:National_Tax>
    <a:Other_Tax>0</a:Other_Tax>
    <a:Summ_Comm_Code>0</a:Summ_Comm_Code>
  </a:fleet_104_data>
  <a:fleet_125_data>
    <a:expNonFuelCodeQty01>0</a:expNonFuelCodeQty01>
    <a:expNonFuelCodeCost01>0</a:expNonFuelCodeCost01>
    <a:expNonFuelCodeQty02>0</a:expNonFuelCodeQty02>
    <a:expNonFuelCodeCost02>0</a:expNonFuelCodeCost02>
    <a:expNonFuelCodeQty03>0</a:expNonFuelCodeQty03>
    <a:expNonFuelCodeCost03>0</a:expNonFuelCodeCost03>
    <a:expNonFuelCodeQty04>0</a:expNonFuelCodeQty04>
    <a:expNonFuelCodeCost04>0</a:expNonFuelCodeCost04>
    <a:expNonFuelCodeQty05>0</a:expNonFuelCodeQty05>
    <a:expNonFuelCodeCost05>0</a:expNonFuelCodeCost05>
    <a:expNonFuelCodeQty06>0</a:expNonFuelCodeQty06>
    <a:expNonFuelCodeCost06>0</a:expNonFuelCodeCost06>
    <a:expNonFuelCodeQty07>0</a:expNonFuelCodeQty07>
    <a:expNonFuelCodeCost07>0</a:expNonFuelCodeCost07>
    <a:expNonFuelCodeQty08>0</a:expNonFuelCodeQty08>
    <a:expNonFuelCodeCost08>0</a:expNonFuelCodeCost08>
  </a:fleet_125_data>
 </tem:TransactionNotificationRequest>
 </soap:Body>
 </soap:Envelope>"""
        //q=T_middleware_base_6_util.strip_namespaces_from_xml(q)
        GPathResult z = new XmlSlurper().parseText(q)
        System.out.println(XmlUtil.serialize(z.Body.TransactionNotificationRequest.fees))

    }

}
