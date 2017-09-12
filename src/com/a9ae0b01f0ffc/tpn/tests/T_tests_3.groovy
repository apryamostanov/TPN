package com.a9ae0b01f0ffc.tpn.tests

import com.a9ae0b01f0ffc.black_box.main.T_logging_base_5_context
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.implementation.T_http_message
import com.a9ae0b01f0ffc.middleware.implementation.T_soap2rest_automated_converter
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import com.a9ae0b01f0ffc.tpn.implementation.T_tpn_http_message
import org.junit.Test

import static com.a9ae0b01f0ffc.commons.implementation.main.T_common_base_1_const.GC_FALSE

class T_tests_3 {

    String z ="""<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:tem="http://tempuri.org/" xmlns:a="https://wp1.wirecard.com/TransactionNotification/Fleet">
 <soap:Header/> 
 <soap:Body> 
 <tem:TransactionNotificationRequest> 
  <a:product>
    <a:productID>1511</a:productID>
    <a:productName>030 - MBL - VISA FLEET - ZAR - 417253</a:productName>
    <a:programManager>Mercantile Bank</a:programManager>
    <a:productCategory>Fleet Card</a:productCategory>
    <a:subProductType>Spend Card Product</a:subProductType>
  </a:product>
  <a:businessAccount>
    <a:sourceBusinessName />
    <a:sourceBusinessAccountNumber />
    <a:businessName>AVIS Car Rental</a:businessName>
    <a:businessAccountNumber>1009000000000004101</a:businessAccountNumber>
  </a:businessAccount>
  <a:card>
    <a:sourceCardNumber />
    <a:sourceProxyNumber>0</a:sourceProxyNumber>
    <a:cardNumber>417253XXXXXX4232</a:cardNumber>
    <a:proxyNumber>1849</a:proxyNumber>
    <a:cardSequenceNumber>0</a:cardSequenceNumber>
  </a:card>
  <a:account>
    <a:sourceAccountNumber>0</a:sourceAccountNumber>
    <a:sourceAccountCurrency />
    <a:sourceAccountBalanceInt />
    <a:sourceAccountBalanceDec />
    <a:AccountNumber>1009000000000004119</a:AccountNumber>
    <a:AccountCurrency>ZAR</a:AccountCurrency>
    <a:AccountBalanceInt>370</a:AccountBalanceInt>
    <a:AccountBalanceDec>72</a:AccountBalanceDec>
  </a:account>
  <a:transaction>
    <a:transactionId>735046</a:transactionId>
    <a:transactionCode>10015</a:transactionCode>
    <a:transactionDescription>Authorization Request</a:transactionDescription>
    <a:transactionTimeStamp>01-Jun-2017 10:10:16 AM</a:transactionTimeStamp>
    <a:transactionLocalDate>01-Jun-2017</a:transactionLocalDate>
    <a:transactionLocalTime>10:10:16</a:transactionLocalTime>
    <a:transactionLifecycleState>O</a:transactionLifecycleState>
    <a:transactionOriginalId>0</a:transactionOriginalId>
    <a:messageTypeIdentifier>0200</a:messageTypeIdentifier>
    <a:transactionResponseCode>00</a:transactionResponseCode>
    <a:debitCreditFlag>+</a:debitCreditFlag>
    <a:logicModule>95</a:logicModule>
    <a:transactionPostingDate>01-Jun-2017</a:transactionPostingDate>
    <a:transactionPostingTime>10:10:22</a:transactionPostingTime>
    <a:postingFlag>0</a:postingFlag>
    <a:postingReference>Overdraft Approved</a:postingReference>
    <a:postingTransactionSource>Visa SMS</a:postingTransactionSource>
    <a:originalTransactionSource>Visa SMS</a:originalTransactionSource>
  </a:transaction>
  <a:network>
    <a:networkName>VS</a:networkName>
    <a:networkTransactionId>317152034961264</a:networkTransactionId>
    <a:networkProcessingCode>00</a:networkProcessingCode>
    <a:networkRRN>715208001263</a:networkRRN>
    <a:networkSTAN>001263</a:networkSTAN>
    <a:networkResponseCode>00</a:networkResponseCode>
  </a:network>
  <a:amounts>
    <a:sourcePostedCurrency />
    <a:sourcePostedAmountInt />
    <a:sourcePostedAmountDec />
    <a:transactionCurrency>ZAR</a:transactionCurrency>
    <a:transactionAmountInt>16</a:transactionAmountInt>
    <a:transactionAmountDec>00</a:transactionAmountDec>
    <a:billingCurrency>ZAR</a:billingCurrency>
    <a:billingAmountInt>16</a:billingAmountInt>
    <a:billingAmountDec>00</a:billingAmountDec>
    <a:postedCurrency>ZAR</a:postedCurrency>
    <a:postedAmountInt>16</a:postedAmountInt>
    <a:postedAmountDec>00</a:postedAmountDec>
    <a:additionalAmountCurrency />
    <a:additionalAmountInt />
    <a:additionalAmountDec />
  </a:amounts>
  <a:fees />
  <a:pos_merchant>
    <a:acquirerId />
    <a:acquirerCountry>ZA</a:acquirerCountry>
    <a:acquirerInstitutionIDCode>12345678901</a:acquirerInstitutionIDCode>
    <a:storeNumber />
    <a:terminalId>TERMID01</a:terminalId>
    <a:terminalType>4</a:terminalType>
    <a:terminalCategory>0</a:terminalCategory>
    <a:terminalEntry>05</a:terminalEntry>
    <a:cardAcceptorIDCode>CARD ACCEPTOR</a:cardAcceptorIDCode>
    <a:cardAcceptorterminalIDCode>TERMID01</a:cardAcceptorterminalIDCode>
    <a:merchantId>CARD ACCEPTOR</a:merchantId>
    <a:merchantCode>5511</a:merchantCode>
    <a:merchantName>ACQUIRER NAME</a:merchantName>
    <a:merchantAddress>ACQUIRER NAME            CITY NAME    ZA</a:merchantAddress>
    <a:merchantCity>CITY NAME</a:merchantCity>
    <a:merchantCountry>ZA</a:merchantCountry>
    <a:merchantType>5511</a:merchantType>
    <a:posEntryMode>5</a:posEntryMode>
    <a:posConditionCode>0</a:posConditionCode>
    <a:posPINCaptureCode />
    <a:nationalPOSGeographicData />
    <a:chipCondition>0</a:chipCondition>
    <a:chipTransactionFlag>1</a:chipTransactionFlag>
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
  <a:additionalData>
    <a:availableBalance>-370.72</a:availableBalance>
    <a:currentBalance>-306.70</a:currentBalance>
    <a:lastCreditAmount>947.24</a:lastCreditAmount>
    <a:lastCreditDate>30-May-2017</a:lastCreditDate>
    <a:lastTransactionDate>01-Jun-2017 10:10:22 AM</a:lastTransactionDate>
    <a:isDeclined>No</a:isDeclined>
    <a:transactionType>Settled</a:transactionType>
    <a:settleAmount>16.00</a:settleAmount>
    <a:settleCurrency>ZAR</a:settleCurrency>
  </a:additionalData>
 </tem:TransactionNotificationRequest>
 </soap:Body> 
 </soap:Envelope>"""

    @Test
    void test_01() {

        T_middleware_base_6_util.init_app_context("C:\\middleware\\conf\\commons.conf")
        T_logging_base_5_context.init_custom(com.a9ae0b01f0ffc.middleware.main.T_middleware_base_5_app_context.app_conf().GC_BLACK_BOX_CONFIG, GC_FALSE, com.a9ae0b01f0ffc.middleware.main.T_middleware_base_5_app_context.app_conf().GC_DYNAMIC_TOKEN_CODE_CLOSURE)
        I_http_message l_rest_message = new T_tpn_http_message()
        I_http_message l_soap_message = new T_tpn_http_message()
        l_soap_message.set_service_name("TPN")
        l_soap_message.set_method("post")
        l_soap_message.set_payload(z)
        HashMap<String, I_http_message> q = new HashMap<String, I_http_message>()
        q.put("TPN",l_soap_message)
        l_rest_message = new T_soap2rest_automated_converter().convert_http_messages(q, "TPN")
        System.out.println(l_rest_message.get_payload())
    }


}
