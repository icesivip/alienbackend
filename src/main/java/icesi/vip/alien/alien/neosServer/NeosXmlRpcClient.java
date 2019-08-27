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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


/**
 * XMLRPC client
 * @author Thawan Kooburat
 *
 */
public class NeosXmlRpcClient implements AsyncCallback {
	private boolean connected = false;
	private boolean waiting;

	private String host;
	private String port;

	private Object response;

	private XmlRpcClient server;
	private Throwable exception;
	private Date startTime;

	final static int sleepInterval = 100; // milliseconds

	public NeosXmlRpcClient(String neosHost, String neosPort) {
		this.host = neosHost;
		this.port = neosPort;

	}

	public boolean connect() throws XmlRpcException {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try {
			config.setServerURL(new URL("https://" + host + ":" + port));
		} catch (MalformedURLException e) {
			throw new XmlRpcException(0, "Malformed URL");
		}

		server = new XmlRpcClient();
		server.setConfig(config);
		connected = true;

		return connected;

	}

	public Object execute(String method, Vector params) throws XmlRpcException {
		try {
			return server.execute(method, params);
		} catch (Exception e) {
			throw new XmlRpcException(0, e.getMessage());
		}
	}

	public Object execute(String method, Vector params, long timeout)
			throws XmlRpcException {
		if (timeout < 0) {
			timeout = 1000000000L; // ~300 hrs
		}
		waiting = true;

		response = null;
		exception = null;
		startTime = new Date();
		server.executeAsync(method, params, this);
		while (waiting
				&& (new Date().getTime() - startTime.getTime() < timeout)) {
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				throw new XmlRpcException(0, "Interrupted");
			}
		}

		if (exception != null)
			throw new XmlRpcException(0, exception.getMessage());
		if (response == null) {
			throw new XmlRpcException(0, "Error communicating with server.");
		}
		return response;
	}

	@Override
	public void handleError(XmlRpcRequest arg0, Throwable error) {

		if (waiting) {
			waiting = false;
			exception = error;
			response = null;
		}
	}

	@Override
	public void handleResult(XmlRpcRequest arg0, Object result) {

		if (waiting) {
			waiting = false;
			response = result;
		}
	}

}
