/*
 * Copyright 2016-2017 Testify Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ibm.gathererservicearchetype.try1;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.ibm.gathererservicearchetype.try1.model.GreetingModel;

/**
 * A service that retrieves an existing greeting.
 *
 * @author saden
 */
public class GetGreeting {

    private final Map<UUID, GreetingModel> store;

    GetGreeting(Map<UUID, GreetingModel> store) {
        this.store = store;
    }

    /**
     * Get a greeting with the given id.
     *
     * @param id the greeting id
     * @return the optional containing the greeting, empty otherwise
     */
    public Optional<GreetingModel> getGreeting(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

}
