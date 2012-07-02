package com.sitecake.contentmanager.client;

import com.sitecake.commons.compile.RawStringValue;

public interface SecuredStringConstants {
	
	/** Raw sitecake version number x.y.z */
	@RawStringValue(value = "${pom.parent.version}", develValue = "1.0.0")
	public String rawVersion();
	
	/**
	 * The sitecake editor version.
	 */
	@RawStringValue(value = "SiteCake ver ${pom.parent.version}", develValue = "SiteCake ver 1.0.0")
	public String version();

	/**
	 * The name/ID of the sitecake provider (e.g. sitecake.com)
	 */
	@RawStringValue(value = "${sitecake.build.hostSystem}")
	public String hostSystem();

	
	/** the second part of the public RSA key */
	@RawStringValue(value = "${sitecake.publicRsaKey.part2}", develValue = "1RkE4RkE2NDY2REE2MjVGNEZERDlDMkU2MEExOERFQ0ExNUY1M0VCMjNFRTlBQTYyNUU1NzFEREQwREEzNTcxQTg5MEJCNDBBRUEyMTFFQUU2")
	// test value
	//@RawStringValue(value = "1RkE4RkE2NDY2REE2MjVGNEZERDlDMkU2MEExOERFQ0ExNUY1M0VCMjNFRTlBQTYyNUU1NzFEREQwREEzNTcxQTg5MEJCNDBBRUEyMTFFQUU2")
	public String licensePubKeyPart2();
	
	/**
	 * The URL used to check if an update is available.
	 * The current version and the host system ID will be reported.
	 */
	@RawStringValue(value = "${sitecake.build.updateCheckURL}", develValue = "http://sitecake.com/services/update/check1")
	// test value
	//@RawStringValue(value = "http://sitecake.com/services/update/check1")
	public String updateCheckUrl();
	
	/** the first part of the public RSA key */
	@RawStringValue(value = "${sitecake.publicRsaKey.part1}", develValue = "QjEwRTkxQjM4NDE2MUQ0NzM1RDI0OTM")
	// test value
	//@RawStringValue(value = "QjEwRTkxQjM4NDE2MUQ0NzM1RDI0OTM")
	public String licensePubKeyPart1();
	
	/** the third part of the RSA key */
	@RawStringValue(value = "${sitecake.publicRsaKey.part3}", develValue = "NEZEQjYwRjYwMDg0Njk2RkFENDU0RUI=")
	// test value
	//@RawStringValue(value = "NEZEQjYwRjYwMDg0Njk2RkFENDU0RUI=")
	public String licensePubKeyPart3(); 
	
	/** the property name (PropertyManager's key) for license dechipered text */
	@RawStringValue(value = "position")
	public String licensePropKey();
	
	/** window.location (window['location']) */
	@RawStringValue(value = "location")
	public String locationObject();
	
	/** window.location.hostname */
	@RawStringValue(value = "hostname")
	public String hostNameProp();

	/** domain prefix/subdomain that is allowed */
	@RawStringValue(value = "www.")
	public String domainPrefix();

}
