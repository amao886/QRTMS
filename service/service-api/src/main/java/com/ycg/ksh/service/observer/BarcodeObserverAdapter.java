/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 12:43:15
 */
package com.ycg.ksh.service.observer;

import com.ycg.ksh.entity.persistent.Barcode;
import com.ycg.ksh.entity.service.barcode.BarcodeContext;

/**
 * 条码观察者
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018-01-09 12:43:15
 */
public interface BarcodeObserverAdapter {

	void notifyBarcodeValidate(Barcode barcode, BarcodeContext validate);
}
