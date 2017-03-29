package com.a9ae0b01f0ffc.tpn.implementation

import com.a9ae0b01f0ffc.black_box.annotations.I_black_box
import com.a9ae0b01f0ffc.black_box.annotations.I_fix_variable_scopes
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_conversion_module
import com.a9ae0b01f0ffc.middleware.Interfaces.I_http_message
import com.a9ae0b01f0ffc.middleware.main.T_middleware_base_6_util
import groovy.transform.ToString
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild

@I_fix_variable_scopes
@ToString(includeNames = true, includeFields = true, includeSuper = false)
class T_wdip2gfs_conversion_module extends T_middleware_base_6_util implements I_http_conversion_module {

    @I_black_box("error")
    /*full previously*/
    @Override
    I_http_message convert_http_message(I_http_message i_http_message) {
        HashMap<String, String> l_business_fields = new HashMap<String, String>()
        I_http_message l_result_http_message = i_http_message
        l_result_http_message.set_payload(make_payload_post(i_http_message.get_payload()))
        return l_result_http_message
    }

    @I_black_box("error")
    /*orig*/
    static ArrayList<NodeChild> find_element(GPathResult i_gpath_result, String i_element_name) {
        return i_gpath_result.depthFirst().findAll { l_element -> l_element.name() == i_element_name }
    }

    @I_black_box("error")
    /*full previously*/
    static String tag(GPathResult i_gpath_result, String i_element_name, String i_name_space = GC_EMPTY_STRING) {
        ArrayList<NodeChild> l_node_children = find_element(i_gpath_result, i_element_name)
        String l_prefix = GC_EMPTY_STRING
        if (i_name_space != GC_EMPTY_STRING) {
            l_prefix += i_name_space + ":"
        }
        if (l_node_children.isEmpty()) {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
        } else {
            GPathResult l_gpath_result = l_node_children.first()
            if (l_gpath_result.text() != GC_EMPTY_STRING) {
                return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + ">" + l_gpath_result.text() + "</" + l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1)
            } else {
                return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
            }
        }
    }

    @I_black_box("error")
    /*full previously*/
    static String tag(HashMap<String, String[]> i_parameter_map, String i_element_name, String i_name_space = GC_EMPTY_STRING) {
        String l_parameter_value = i_parameter_map.get(i_element_name)?.getAt(GC_FIRST_INDEX)
        String l_prefix = GC_EMPTY_STRING
        if (i_name_space != GC_EMPTY_STRING) {
            l_prefix += i_name_space + ":"
        }
        if (is_not_null(l_parameter_value)) {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + ">" + l_parameter_value + "</" + l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1)
        } else {
            return l_prefix + i_element_name.substring(i_element_name.lastIndexOf(".") + 1) + " i:nil=\"true\"/"
        }
    }

    @I_black_box("error")
    /*full previously*/
    static String make_payload_post(String i_xml_payload_orig) {
        GPathResult l_gpath_result = new XmlSlurper().parseText(i_xml_payload_orig)
        String l_payload = """<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
   <s:Body>
      <TranHistNotification xmlns="http://tempuri.org/">
           <TranHistNotificationRequest xmlns:a="http://schemas.datacontract.org/2004/07/WDNotifyServices.DTO" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
            <a:AvailableBalance>${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceDec.text()}</a:AvailableBalance>
            <a:CurrentBalance>${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceDec.text()}</a:CurrentBalance>
            <a:FilterResultCount>1</a:FilterResultCount>
            <a:LastCreditAmount/>
            <a:LastCreditDate/>
            <a:LastTransactionDate/>
            <a:TransactionHistoryList>
               <a:TransactionsHistory>
                  <a:AccountNumber>${l_gpath_result.Body.TransactionNotificationRequest.account.AccountNumber.text()}</a:AccountNumber>
                  <a:BillingCurrencyCode>${currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.billingCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))}</a:BillingCurrencyCode>
                  <a:CMTTranType>94</a:CMTTranType>
                  <a:CardAcceptorNameLocation>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantName.text()}</a:CardAcceptorNameLocation>
                  <a:CardMasking>${l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text()}</a:CardMasking>
                  <a:CardNumber4Digits>${last_chars(l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text(), GC_FOUR_CHARS)}</a:CardNumber4Digits>
                  <a:Current_Balance>${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.account.AccountBalanceDec.text()}</a:Current_Balance>
                  <a:CustomAcctID/>
                  <a:IsDeclined>${l_gpath_result.Body.TransactionNotificationRequest.transaction.postingReference.text()=="Transaction posted successfully"?"no":"yes"}</a:IsDeclined>
                  <a:MerchantCity>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantCity.text()}</a:MerchantCity>
                  <a:PostTime>${d2s(s2d(l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalDate.text(), "dd-MMM-yyyy"), "yyyy-MM-dd")}T${l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalTime.text()}</a:PostTime>
                  <a:PostingRef>${l_gpath_result.Body.TransactionNotificationRequest.transaction.postingReference.text()}</a:PostingRef>
                  <a:ProductBin>${l_gpath_result.Body.TransactionNotificationRequest.card.cardNumber.text().substring(0, 6)}</a:ProductBin>
                  <a:ProxyNumber>${l_gpath_result.Body.TransactionNotificationRequest.card.proxyNumber.text()}</a:ProxyNumber>
                  <a:SpendCategory>Miscellaneous</a:SpendCategory>
                  <a:TranId>${l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionId.text()}</a:TranId>
                  <a:TranTime>${d2s(s2d(l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalDate.text(), "dd-MMM-yyyy"), "yyyy-MM-dd")}T${l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionLocalTime.text()}</a:TranTime>
                  <a:TransactionAmount>${l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionAmountInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionAmountDec.text()}</a:TransactionAmount>
                  <a:TransactionCurrencyCode>${currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.transactionCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))}</a:TransactionCurrencyCode>
                  <a:TransactionDescription>${l_gpath_result.Body.TransactionNotificationRequest.transaction.transactionDescription.text()}</a:TransactionDescription>
                  <a:TransactionReason/>
                  <a:TransactionType>Settled</a:TransactionType>
                  <a:TxnSource>Visa SMS</a:TxnSource>
                  <a:TypeOfTran>Tran</a:TypeOfTran>
                  <a:MCC>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.terminalCategory.text()}</a:MCC>
                  <a:MerchantName>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantName.text()}</a:MerchantName>
                  <a:MerchantCountry>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantCountry.text()}</a:MerchantCountry>
                  <a:MerchantPostalCode/>
                  <a:MerchantAddress>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantAddress.text()}</a:MerchantAddress>
                  <a:BillingAmount>${l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountDec.text()}</a:BillingAmount>
                  <a:SettleAmount/>
                  <a:SettleCurrency/>
                  <a:TerminalID>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.terminalId.text()}</a:TerminalID>
                  <a:TerminalType>E</a:TerminalType>
                  <a:TraceNum>${l_gpath_result.Body.TransactionNotificationRequest.network.networkSTAN.text()}</a:TraceNum>
                  <a:PostedAmount>${l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountInt.text()}.${l_gpath_result.Body.TransactionNotificationRequest.amounts.billingAmountDec.text()}</a:PostedAmount>
                  <a:PostedCurrency>${currency_digits(l_gpath_result.Body.TransactionNotificationRequest.amounts.postedCurrency.text().padLeft(GC_THREE_CHARS, GC_ZERO_CHAR))}</a:PostedCurrency>
                  <a:MerchantId>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.merchantId.text()}</a:MerchantId>
                  <a:RRN>${l_gpath_result.Body.TransactionNotificationRequest.network.networkRRN.text()}</a:RRN>
                  <a:AcquirerCountry>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.acquirerCountry.text()}</a:AcquirerCountry>
                  <a:AcquirerID>${l_gpath_result.Body.TransactionNotificationRequest.pos_merchant.acquirerId.text()}</a:AcquirerID>
               </a:TransactionsHistory>
            </a:TransactionHistoryList>
            <a:UniqueID/>
            <a:UniqueIDflag>0</a:UniqueIDflag>
         </TranHistNotificationRequest>
      </TranHistNotification>
   </s:Body>
</s:Envelope>"""
         return l_payload
    }

    @I_black_box("error")
    /*full previously*/
    static String get_service_name(I_http_message i_http_request) {
        String l_service_name = GC_EMPTY_STRING
        URL l_url = new URL(i_http_request.get_url())
        String l_path = l_url.getPath()
        l_service_name = l_path.substring(l_path.lastIndexOf("/") + GC_ONE_CHAR)
        return l_service_name
    }

}
