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
 * Interface for class which need to be notified when result is ready for processing 
 * @author Thawan Kooburat
 *
 */
public interface ResultCallback {
	
	/**
	 * This method will be called when job is submitted;
	 * @param results	output text from NEOS
	 * @param jobNo
	 * @param pass
	 */
	public void handleJobInfo(int jobNo, String pass);
	
	/**
	 * This method will be called when final result from NEOS is ready
	 * @param results	output text from NEOS
	 * @param jobNo
	 * @param pass
	 */
	
	public void handleFinalResult(String results);
}
