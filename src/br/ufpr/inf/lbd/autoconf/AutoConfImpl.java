/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.ufpr.inf.lbd.autoconf;

import java.io.FileInputStream;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class AutoConfImpl implements AutoConf {
  Index index;

  public AutoConfImpl() throws RemoteException {
    this.index = new Index();
  }

  public TuningKnobs autoConfigure(TuningKnobs k) throws RemoteException {
    return index.getTunedKnobs(k);
  }

  public boolean add(TuningKnobs k) throws RemoteException {
    return index.add(k);
  }

  public boolean remove(TuningKnobs k) throws RemoteException {
    return index.remove(k);
  }

  public boolean update(TuningKnobs k) throws RemoteException {
    return index.update(k);
  }

  public void list() throws RemoteException {
    index.list();
  }

  public void show(TuningKnobs k) throws RemoteException {
    index.show(k);
  }

  public static void main(String args[]) {
    if (System.getSecurityManager() == null)
      System.setSecurityManager(new RMISecurityManager());

    try {
      Properties rmiconfig = new Properties();
      FileInputStream in = new FileInputStream("autoconf.conf");
      rmiconfig.load(in);

      AutoConfImpl server = new AutoConfImpl();
      AutoConf stub = (AutoConf) UnicastRemoteObject.exportObject(server, 0);
      Registry registry = LocateRegistry.createRegistry(50123);
      registry.rebind("//" + rmiconfig.getProperty("RMI_SERVER") + "/AutoConf", stub);
      in.close();

      System.out.println("* AutoConf server is running at " +  rmiconfig.getProperty("RMI_SERVER") + ".");
    } catch (Exception e) {
      System.err.println("AutoConf error: " + e.getMessage());
      e. printStackTrace();
    }
  }
}
