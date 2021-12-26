package com.ntredize.redstop.support.service;

import android.content.Context;

import com.ntredize.redstop.R;

public class ScanBarcodeService {
	
	private final Context context;

	
	/* Init */
	public ScanBarcodeService(Context context) {
		this.context = context;
	}
	
	
	/* Main */
	public boolean validateBarcodeFormat(String barcode) {
		return isEan13Barcode(barcode) || isUpcaBarcode(barcode);
	}
	
	public String findCountryByBarcode(String barcode) {
		// country code
		Integer countryCode = null;
		if (isEan13Barcode(barcode)) countryCode = getCountryCodeFromEan13Barcode(barcode);
        else if (isUpcaBarcode(barcode)) countryCode = getCountryCodeFromUpcaBarcode(barcode);
		
		// company name
		return context.getString(getKeyByCountryCode(countryCode));
	}
	
	
	/* EAN-13 */
	private boolean isEan13Barcode(String barcode) {
		// length is 13
		// all number
		return barcode.matches("^[0-9]{13}$");
	}
	
	private Integer getCountryCodeFromEan13Barcode(String barcode) {
		return Integer.valueOf(barcode.substring(0, 3));
	}
	
	
	/*  UPC-A */
	private boolean isUpcaBarcode(String barcode) {
		// length is 12
		// all number
		return barcode.matches("^[0-9]{12}$");
	}
	
	private Integer getCountryCodeFromUpcaBarcode(String barcode) {
		return Integer.valueOf(barcode.substring(0, 2));
	}
	
	
	/* Company Name */
	private Integer getKeyByCountryCode(Integer countryCode) {
		if (countryCode != null) {
			if (countryCode >= 0 && countryCode <= 19) return R.string.country_000_019_united_states_canada;
			else if (countryCode >= 30 && countryCode <= 39) return R.string.country_030_039_united_states;
			else if (countryCode >= 60 && countryCode <= 99) return R.string.country_060_099_united_states_canada;
			else if (countryCode >= 100 && countryCode <= 139) return R.string.country_100_139_united_states;
			else if (countryCode >= 300 && countryCode <= 379) return R.string.country_300_379_france_monaco;
			else if (countryCode == 380) return R.string.country_380_bulgaria;
			else if (countryCode == 383) return R.string.country_383_slovenia;
			else if (countryCode == 385) return R.string.country_385_croatia;
			else if (countryCode == 387) return R.string.country_387_bosnia;
			else if (countryCode == 389) return R.string.country_389_montenegro;
			else if (countryCode == 390) return R.string.country_390_kosovo;
			else if (countryCode >= 400 && countryCode <= 440) return R.string.country_400_440_germany;
			else if (countryCode >= 450 && countryCode <= 459) return R.string.country_450_459_japan;
			else if (countryCode >= 460 && countryCode <= 469) return R.string.country_460_469_russia;
			else if (countryCode == 470) return R.string.country_470_kyrgyzstan;
			else if (countryCode == 471) return R.string.country_471_taiwan;
			else if (countryCode == 474) return R.string.country_474_estonia;
			else if (countryCode == 475) return R.string.country_475_latvia;
			else if (countryCode == 476) return R.string.country_476_azerbaijan;
			else if (countryCode == 477) return R.string.country_477_lithuania;
			else if (countryCode == 478) return R.string.country_478_uzbekistan;
			else if (countryCode == 479) return R.string.country_479_sri_lanka;
			else if (countryCode == 480) return R.string.country_480_philippines;
			else if (countryCode == 481) return R.string.country_481_belarus;
			else if (countryCode == 482) return R.string.country_482_ukraine;
			else if (countryCode == 483) return R.string.country_483_turkmenistan;
			else if (countryCode == 484) return R.string.country_484_moldova;
			else if (countryCode == 485) return R.string.country_485_armenia;
			else if (countryCode == 486) return R.string.country_486_georgia;
			else if (countryCode == 487) return R.string.country_487_kazakhstan;
			else if (countryCode == 488) return R.string.country_488_tajikistan;
			else if (countryCode == 489) return R.string.country_489_hong_kong;
			else if (countryCode >= 490 && countryCode <= 499) return R.string.country_490_499_japan;
			else if (countryCode >= 500 && countryCode <= 509) return R.string.country_500_509_united_kingdom;
			else if (countryCode >= 520 && countryCode <= 521) return R.string.country_520_521_greece;
			else if (countryCode == 528) return R.string.country_528_lebanon;
			else if (countryCode == 529) return R.string.country_529_cyprus;
			else if (countryCode == 530) return R.string.country_530_albania;
			else if (countryCode == 531) return R.string.country_531_macedonia;
			else if (countryCode == 535) return R.string.country_535_malta;
			else if (countryCode == 539) return R.string.country_539_lreland;
			else if (countryCode >= 540 && countryCode <= 549) return R.string.country_540_549_belgium_luxembourg;
			else if (countryCode == 560) return R.string.country_560_portugal;
			else if (countryCode == 569) return R.string.country_569_iceland;
			else if (countryCode >= 570 && countryCode <= 579) return R.string.country_570_579_denmark_faroe_islands_greenland;
			else if (countryCode == 590) return R.string.country_590_poland;
			else if (countryCode == 594) return R.string.country_594_romania;
			else if (countryCode == 599) return R.string.country_599_hungary;
			else if (countryCode >= 600 && countryCode <= 601) return R.string.country_600_601_south_africa;
			else if (countryCode == 603) return R.string.country_603_ghana;
			else if (countryCode == 604) return R.string.country_604_senegal;
			else if (countryCode == 608) return R.string.country_608_bahrain;
			else if (countryCode == 609) return R.string.country_609_mauritius;
			else if (countryCode == 611) return R.string.country_611_morocco;
			else if (countryCode == 613) return R.string.country_613_algeria;
			else if (countryCode == 615) return R.string.country_615_nigeria;
			else if (countryCode == 616) return R.string.country_616_kenya;
			else if (countryCode == 617) return R.string.country_617_cameroon;
			else if (countryCode == 618) return R.string.country_618_ivory_coast;
			else if (countryCode == 619) return R.string.country_619_tunisia;
			else if (countryCode == 620) return R.string.country_620_tanzania;
			else if (countryCode == 621) return R.string.country_621_syria;
			else if (countryCode == 622) return R.string.country_622_egypt;
			else if (countryCode == 623) return R.string.country_623_brunei;
			else if (countryCode == 624) return R.string.country_624_libya;
			else if (countryCode == 625) return R.string.country_625_jordan;
			else if (countryCode == 626) return R.string.country_626_iran;
			else if (countryCode == 627) return R.string.country_627_kuwait;
			else if (countryCode == 628) return R.string.country_628_saudi_arabia;
			else if (countryCode == 629) return R.string.country_629_emirates;
			else if (countryCode == 630) return R.string.country_630_qatar;
			else if (countryCode >= 640 && countryCode <= 649) return R.string.country_640_649_finland;
			else if (countryCode >= 690 && countryCode <= 699) return R.string.country_690_699_china;
			else if (countryCode >= 700 && countryCode <= 709) return R.string.country_700_709_norway;
			else if (countryCode == 729) return R.string.country_729_israel;
			else if (countryCode >= 730 && countryCode <= 739) return R.string.country_730_739_sweden;
			else if (countryCode == 740) return R.string.country_740_guatemala;
			else if (countryCode == 741) return R.string.country_741_el_salvador;
			else if (countryCode == 742) return R.string.country_742_honduras;
			else if (countryCode == 743) return R.string.country_743_nicaragua;
			else if (countryCode == 744) return R.string.country_744_costa_rica;
			else if (countryCode == 745) return R.string.country_745_panama;
			else if (countryCode == 746) return R.string.country_746_dominican;
			else if (countryCode == 750) return R.string.country_750_mexico;
			else if (countryCode >= 754 && countryCode <= 755) return R.string.country_754_755_canada;
			else if (countryCode == 759) return R.string.country_759_venezuela;
			else if (countryCode >= 760 && countryCode <= 769) return R.string.country_760_769_switzerland_liechtenstein;
			else if (countryCode >= 770 && countryCode <= 771) return R.string.country_770_771_colombia;
			else if (countryCode == 773) return R.string.country_773_uruguay;
			else if (countryCode == 775) return R.string.country_775_peru;
			else if (countryCode == 777) return R.string.country_777_bolivia;
			else if (countryCode >= 778 && countryCode <= 779) return R.string.country_778_779_argentina;
			else if (countryCode == 780) return R.string.country_780_chile;
			else if (countryCode == 784) return R.string.country_784_paraguay;
			else if (countryCode == 786) return R.string.country_786_ecuador;
			else if (countryCode >= 789 && countryCode <= 790) return R.string.country_789_790_brazil;
			else if (countryCode >= 800 && countryCode <= 839) return R.string.country_800_839_italy_san_marino_vatican_city;
			else if (countryCode >= 840 && countryCode <= 849) return R.string.country_840_849_spain_andorra;
			else if (countryCode == 850) return R.string.country_850_cuba;
			else if (countryCode == 858) return R.string.country_858_slovakia;
			else if (countryCode == 859) return R.string.country_859_czech;
			else if (countryCode == 860) return R.string.country_860_serbia;
			else if (countryCode == 865) return R.string.country_865_mongolia;
			else if (countryCode == 867) return R.string.country_867_north_korea;
			else if (countryCode >= 868 && countryCode <= 869) return R.string.country_868_869_turkey;
			else if (countryCode >= 870 && countryCode <= 879) return R.string.country_870_879_netherlands;
			else if (countryCode == 880) return R.string.country_880_south_korea;
			else if (countryCode == 883) return R.string.country_883_myanmar;
			else if (countryCode == 884) return R.string.country_884_cambodia;
			else if (countryCode == 885) return R.string.country_885_thailand;
			else if (countryCode == 888) return R.string.country_888_singapore;
			else if (countryCode == 890) return R.string.country_890_india;
			else if (countryCode == 893) return R.string.country_893_vietnam;
			else if (countryCode == 896) return R.string.country_896_pakistan;
			else if (countryCode == 899) return R.string.country_899_indonesia;
			else if (countryCode >= 900 && countryCode <= 919) return R.string.country_900_919_austria;
			else if (countryCode >= 930 && countryCode <= 939) return R.string.country_930_939_australia;
			else if (countryCode >= 940 && countryCode <= 949) return R.string.country_940_949_new_zealand;
			else if (countryCode == 955) return R.string.country_955_malaysia;
			else if (countryCode == 958) return R.string.country_958_macau;
		}
		return R.string.country_unknown;
	}
	
}
