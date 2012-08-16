/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.slc.sli.modeling.uml;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test for AssociationEnd
 * @author chung
 */
public class AssociationEndTest {

    private AssociationEnd associationEnd;
    private Identifier identifier = Identifier.random();
    private Range range = new Range(Occurs.ZERO, Occurs.ONE);

    @Before
    public void setup() {
        associationEnd = new AssociationEnd(new Multiplicity(range), "TestAssocEnd", false, identifier);
    }

    @Test
    public void testIsAttribute() {
        assertEquals(associationEnd.isAttribute(), false);
    }

    @Test
    public void testIsAssociationEnd() {
        assertEquals(associationEnd.isAssociationEnd(), true);
    }

    @Test
    public void testIsNavigable() {
        assertEquals(associationEnd.isNavigable(), false);
    }

    @Test
    public void testToString() {
        String string1 = associationEnd.toString();
        String string2 = "{id: " + associationEnd.getId() + ", name: TestAssocEnd, type: " + identifier
                + ", multiplicity: " + new Multiplicity(range) + "}";
        assertEquals(string1, string2);
    }

}
