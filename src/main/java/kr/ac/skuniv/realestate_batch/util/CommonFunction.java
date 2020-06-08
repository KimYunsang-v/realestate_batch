package kr.ac.skuniv.realestate_batch.util;

import java.util.Date;
import java.util.GregorianCalendar;

import kr.ac.skuniv.realestate_batch.domain.dto.abstractDto.ItemDto;

public final class CommonFunction {

	public static Date getDate(ItemDto itemDto){
		return new GregorianCalendar(itemDto.getYear(), itemDto.getMonthly() - 1,
			Integer.parseInt(itemDto.getDays())).getTime();
	}

	public static String removeMoneyBlank(String money){
		return money.trim().replaceAll("[^0-9?!\\.]","");
	}

	public static int getCityCode(String regionCode){
		return Integer.parseInt(regionCode.substring(0,2));
	}

	public static int getGroopCode(String regionCode){
		return Integer.parseInt(regionCode.substring(2));
	}
}
