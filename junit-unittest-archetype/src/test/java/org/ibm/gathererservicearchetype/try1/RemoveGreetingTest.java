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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.testifyproject.annotation.Fake;
import org.testifyproject.annotation.Name;
import org.testifyproject.annotation.Sut;
import org.testifyproject.junit4.UnitTest;

import org.ibm.gathererservicearchetype.try1.model.GreetingModel;

/**
 * A unit test that demonstrates discovery of collaborators based on an explictly defined
 * collaborator name. Notice that the name of the collaborator in this test class is "store"
 * where as the name of the collaborator in {@link RemoveGreeting} is "datastore".
 *
 * @author saden
 */
@RunWith(UnitTest.class)
public class RemoveGreetingTest {

    @Sut
    RemoveGreeting sut;

    @Name("datastore")
    @Fake
    Map<UUID, GreetingModel> store;

    @Test
    public void givenMapStoreNewRemoveGreetingShouldNotDoWorkInConstructor() {
        //Act
        RemoveGreeting result = new RemoveGreeting(store);

        //Assert
        assertThat(result).isNotNull();
        verifyZeroInteractions(store);
    }

    @Test
    public void givenNullRemoveGreetingShouldDoNothing() {
        //Arrange
        UUID id = null;
        GreetingModel greeting = null;
        given(store.remove(id)).willReturn(greeting);

        //Act
        sut.removeGreeting(id);

        //Assert
        verify(store).remove(id);
    }

    @Test
    public void givenNoneExistentidRemoveGreetingShouldDoNothing() {
        //Arrange
        UUID id = UUID.fromString("0d216415-1b8e-4ab9-8531-fcbd25d5966f");
        GreetingModel greeting = null;
        given(store.remove(id)).willReturn(greeting);

        //Act
        sut.removeGreeting(id);

        //Assert
        verify(store).remove(id);
    }

    @Test
    public void givenExistingidRemoveGreetingShouldShouldRemoveGreeting() {
        //Arrange
        UUID id = UUID.fromString("0d216415-1b8e-4ab9-8531-fcbd25d5966f");
        GreetingModel greeting = mock(GreetingModel.class);
        given(store.remove(id)).willReturn(greeting);

        //Act
        sut.removeGreeting(id);

        //Assert
        verify(store).remove(id);
    }

}
