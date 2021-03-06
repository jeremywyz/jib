/*
 * Copyright 2018 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.tools.jib.registry;

import com.google.cloud.tools.jib.api.RegistryException;
import com.google.cloud.tools.jib.event.EventDispatcher;
import com.google.cloud.tools.jib.http.Authorization;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/** Integration tests for {@link AuthenticationMethodRetriever}. */
public class AuthenticationMethodRetrieverIntegrationTest {

  private static final EventDispatcher EVENT_DISPATCHER = jibEvent -> {};

  @Test
  public void testGetRegistryAuthenticator() throws IOException, RegistryException {
    RegistryClient registryClient =
        RegistryClient.factory(EVENT_DISPATCHER, "registry.hub.docker.com", "library/busybox")
            .newRegistryClient();
    RegistryAuthenticator registryAuthenticator = registryClient.getRegistryAuthenticator();
    Assert.assertNotNull(registryAuthenticator);
    Authorization authorization = registryAuthenticator.authenticatePull(null);

    RegistryClient authorizedRegistryClient =
        RegistryClient.factory(EVENT_DISPATCHER, "registry.hub.docker.com", "library/busybox")
            .setAuthorization(authorization)
            .newRegistryClient();
    authorizedRegistryClient.pullManifest("latest");
  }
}
