package ProblemPractice;

public class microFocus {
	public boolean validateIpAddress(String ipAddress, String cidrRange) throws IllegalArgumentException {
		if (ipAddress == null || ipAddress.length() == 0) {
			return false;
		}

		if (cidrRange == null || cidrRange.length() == 0) {
			return false;
		}
		/*Function to validate given IP address is valid or not*/
		if (!(helperIpAddressValidate(ipAddress))) {
			/*throws exception when given IPAddress definition is not valid*/
			throw new IllegalArgumentException();
		}
		
		/*Function to validate given CidrRange is in IP format also to check the given IPAddress falls with in the cidrRange*/
		if (!(helpercidrRangeValidate(ipAddress, cidrRange))) {
			return false;
		}
		return true;
	}

	public boolean helperIpAddressValidate(String ipAddress) {
		if (!(ipAddress.contains("."))) {
			return false;
		}
		String[] ipAddr = ipAddress.split("\\.");

		if (ipAddr.length != 4) {
			return false;
		}

		for (int i = 0; i < ipAddr.length; i++) {
			try {
				int num = Integer.parseInt(ipAddr[i]);
				if (!(num >= 0 && num <= 255)) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public boolean helpercidrRangeValidate(String ipAddress, String cidrRange) {
		if (!(cidrRange.contains("/"))) {
			/*append 32 if not given*/
			cidrRange += "/32";
		}

		String[] rangeSplit = cidrRange.split("/");
		String rangeIpAddr = rangeSplit[0];
		String[] ipAdrrGiven = ipAddress.split("\\.");
		String[] ipAdrrSplit = rangeIpAddr.split("\\.");
		if (!(helperIpAddressValidate(rangeIpAddr))) {
			throw new IllegalArgumentException();
		}
		int range = Integer.parseInt(rangeSplit[1]);
		int rangeChange = 32 - range;
		if (range > 32) {
			return false;
		}

		if (range == 32) {
			if (!(ipAddress.equals(rangeIpAddr))) {
				return false;
			} else {
				return true;
			}
		}

		int i = 0;
		/*This loop is to compare the common prefix range*/
		while (range > 0) {
			if (range >= 8) {
				int mask = 255;
				int first = Integer.parseInt(ipAdrrGiven[i]) & mask;
				int second = Integer.parseInt(ipAdrrSplit[i]) & mask;

				if (first != second) {
					return false;
				}
				i++;
				range = range - 8;
			} else {
				int mask = (int) Math.pow(2, range) - 1;
				mask = mask << (8-range);
				int first = Integer.parseInt(ipAdrrGiven[i]) & mask;
				int second = Integer.parseInt(ipAdrrSplit[i]) & mask;
				if (first != second) {
					return false;
				}
				range = 0;
			}
		}

		/*This is to check the remaining bits from given IPAddress with in the of cidrRange*/
		int j = ipAdrrGiven.length - 1;
		while (rangeChange > 0) {
			if (rangeChange >= 8) {
				/*if (!(Integer.parseInt(ipAdrrGiven[j]) >= Integer.parseInt(ipAdrrSplit[j])
						&& Integer.parseInt(ipAdrrGiven[j]) < 256)) {
					return false;
				}*/
				
				if (!(Integer.parseInt(ipAdrrGiven[j]) >= 0
						&& Integer.parseInt(ipAdrrGiven[j]) < 256)) {
					return false;
				}
				j--;
				rangeChange = rangeChange - 8;
			} else {
				int mask = (int) Math.pow(2, rangeChange) - 1;
				int first = Integer.parseInt(ipAdrrGiven[j]) & mask;
				int second = Integer.parseInt(ipAdrrSplit[j]) & mask;	
				/*if (!(first >= second && first <= mask)) {
					return false;
				}*/
				
				if (!(first >= 0 && first <= mask)) {
					return false;
				}
				
				rangeChange = 0;
			}
		}

		return true;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		/*TC-1 Valid IP & cidrRange*/
		printTest("192.168.0.0","192.168.0.0/32");
		/*TC-2 slash omitted*/
		printTest("192.168.0.0","192.168.0.0");
		/*TC - 3 , in IPAddress - invalid*/
		printTest("111.122.0,0","111.122.0,0");
		/*TC- 4 Invalid cidrRange*/
		printTest("111.122.0.0","133,155.0,0");
		/*TC-5 IPAddress with in cidrRange*/
		printTest("101.100.0.254","101.100.0.1/24");
		/*TC-6  IPAddress is with cidrRange*/
		printTest("192.168.0.254","192.168.1.1/20");
		/*TC-7 the IPAddr is valid*/
		printTest("192.168.4.254","192.168.3.254/20");
		/*TC-8 the ip address not in cidrRange accepts range from 192.168.0.0 - 192.168.31.255*/
		printTest("192.168.33.254","192.168.3.254/19");
		/*TC- 9 IPAddress out of cidrRange accepts from 121.168.0.0 -121.168.127.255*/
		printTest("121.168.200.252","121.168.5.254/17");
		/*TC-10 IP address contains value > 255*/
		printTest("257.168.0.254","192.168.1.1/20");
		/*TC-11 cidrRange contains value > 255*/
		printTest("255.1.0.254","255.256.1.1/20");
		
	}
	
	public static void printTest(String ipAddress, String cidrRange) {
		microFocus m = new microFocus();
		try {
			boolean result = m.validateIpAddress(ipAddress, cidrRange);
			System.out.println("result:->" + result);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
	}

}
