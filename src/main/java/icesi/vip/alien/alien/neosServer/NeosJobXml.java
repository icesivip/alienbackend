/*
 * Copyright (c) 2017 NEOS-Server
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package icesi.vip.alien.alien.neosServer;

/**
 * Class for creating Job description XML for NEOS
 * 
 * @author Thawan Kooburat
 * 
 */
public class NeosJobXml {

	private StringBuffer xml = new StringBuffer();

	public NeosJobXml(String category, String solver, String input) {

		xml.append("<document>\n<category>" + category
				+ "</category>\n<solver>" + solver + "</solver>\n<inputMethod>"
				+ input + "</inputMethod>\n");

	}

	/**
	 * Add parameter to job description
	 * 
	 * @param name
	 * @param value
	 */
	public void addParam(String name, String value) {

		xml.append("<" + name + ">\n<![CDATA[\n" + value + "\n]]>\n</" + name
				+ ">\n");
	}

	/**
	 * Add binary parameter to job description (will be converted to Base64)
	 * 
	 * @param name
	 * @param value
	 */
	public void addBinaryParam(String name, byte[] value) {
		addBinaryParam(name, value, 0, value.length);
	}

	/**
	 * Add binary parameter to job description (will be converted to Base64)
	 * 
	 * @param name
	 * @param value
	 */
	public void addBinaryParam(String name, byte[] value, int offset, int len) {

		xml.append("<" + name + "><base64>\n");
		xml.append(Base64.encodeBytes(value, offset, len));
		xml.append("</base64></" + name + ">\n");
	}

	/**
	 * Create XML of this job
	 * 
	 * @return
	 */
	public String toXMLString() {
		return xml.toString() + "</document>";
	}
}
