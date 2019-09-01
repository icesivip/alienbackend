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

import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

public class NeosClient {
	private String host;
	private String port;

	NeosXmlRpcClient server = null;

	public NeosClient(String host, String port) {
		this.host = host;
		this.port = port;

		server = new NeosXmlRpcClient(host, port);

		if (server != null) {
			try {
				server.connect();
			} catch (XmlRpcException e) {
				// TODO Auto-generated catch block
				System.err.printf("Unable to connect to server %s:%s\n%s",
						host, port, e.getMessage());
				server = null;

			}

		}
	}

	/**
	 * Simple job submission interface.
	 * 
	 * @param jobXML
	 *            String containing NEOS's job xml
	 * @return null if there is an error during job submission
	 */
	public NeosJob submitJob(String jobXML) {
		if (server == null)
			return null;

		Vector params = new Vector();
		params.add(jobXML);

		Integer jobNo = 0;
		String jobPass = "";

		String result = "";

		try {

			Object[] results = (Object[]) server.execute("submitJob", params,
					5000);

			jobNo = (Integer) results[0];
			jobPass = (String) results[1];

			params = new Vector();
			params.add(jobNo);
			params.add(jobPass);

			Object retval = server.execute("getFinalResults", params);

			if (retval instanceof String) {
				result = (String) retval;
			} else if (retval instanceof byte[]) {
				result = (new String((byte[]) retval));

			}

		} catch (XmlRpcException e) {
			System.err.println("Error submitting job\n" + e.getMessage());
			return null;
		}

		NeosJob job = new NeosJob();
		job.setJobNo(jobNo);
		job.setJobPass(jobPass);
		job.setResult(result);

		return job;
	}

	/**
	 * Non-blocking version of job submission method
	 * 
	 * @param jobXML
	 * @param callback
	 *            Object that implement result callback interface
	 * @return NeosJob which contains job number and job password but not the
	 *         result.
	 */
	public NeosJob submitJobNonBlocking(String jobXML, ResultCallback callback) {
		if (server == null)
			return null;

		ResultReceiver receiver;

		Vector params = new Vector();
		params.add(jobXML);

		Integer jobNo = 0;
		String jobPass = "";

		String result = "";

		try {

			Object[] results = (Object[]) server.execute("submitJob", params,
					5000);

			jobNo = (Integer) results[0];
			jobPass = (String) results[1];

		} catch (XmlRpcException e) {
			System.err.println("Error submitting job\n" + e.getMessage());
			return null;
		}

		receiver = new ResultReceiver(server, callback, jobNo, jobPass);
		receiver.start();

		NeosJob job = new NeosJob();
		job.setJobNo(jobNo);
		job.setJobPass(jobPass);

		return job;
	}

	/**
	 * Get content of results.txt file of a given job
	 * 
	 * @param jobNo
	 *            job number
	 * @param jobPass
	 *            job password
	 * @return content of "results.txt" or empty string if file is not found
	 */
	public String getResultsFile(int jobNo, String jobPass) {
		if (server == null)
			return "";
		String result = "";

		Vector params = new Vector();
		params.add(jobNo);
		params.add(jobPass);

		// Wait until job is done
		try {
			Object ret;

			String neosStatus = "";
			while (!neosStatus.equals("Done")) {
				neosStatus = (String) server.execute("getJobStatus", params);
				Thread.sleep(10);
			}

			// Add file name that we want to get
			params.add("results.txt");

			ret = server.execute("getOutputFile", params);

			if (ret instanceof String) {
				result = (String) ret;
			} else if (ret instanceof byte[]) {
				result = (new String((byte[]) ret));

			}

		} catch (XmlRpcException e) {
			System.err.println("Error submitting job\n" + e.getMessage());

		} catch (InterruptedException e) {
			System.err.println("User interrupted \n" + e.getMessage());

		}

		if (result.equals("Output file not exists"))
			result = "";

		return result;

	}
}
