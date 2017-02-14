package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.main.T_s
import com.a9ae0b01f0ffc.tpn.main.T_tpn_const
import com.a9ae0b01f0ffc.tpn.main.T_tpn_s


class T_tpn {

    @I_black_box
    void send() throws Exception {

        URL l_url = new URL(T_tpn_s.c().GC_TPN_URL)
        HttpURLConnection l_https_url_connection = (HttpURLConnection) l_url.openConnection()
        l_https_url_connection.setRequestMethod("POST")
        l_https_url_connection.setRequestProperty("User-Agent", T_tpn_s.c().GC_USER_AGENT)
        l_https_url_connection.setRequestProperty("Accept-Language", T_tpn_s.c().GC_ACCEPT_LANGUAGE)
        l_https_url_connection.setRequestProperty("Content-Type", T_tpn_s.c().GC_CONTENT_TYPE)
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
        l_https_url_connection.setDoOutput(T_tpn_const.GC_TRUE)
        DataOutputStream l_data_output_stream = new DataOutputStream(l_https_url_connection.getOutputStream())
        l_data_output_stream.writeBytes(l_output_data)
        l_data_output_stream.flush()
        l_data_output_stream.close()
        Integer l_response_code = l_https_url_connection.getResponseCode()
        BufferedReader l_buffered_reader = new BufferedReader(new InputStreamReader(l_https_url_connection.getInputStream()))
        String inputLine
        StringBuffer response = new StringBuffer()
        while ((inputLine = l_buffered_reader.readLine()) != null) {
            response.append(inputLine)
        }
        l_buffered_reader.close()
        T_s.l().log_info(T_s.s().Response, response)
    }

}
