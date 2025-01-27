/*
Copyright 2020 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package io.kubernetes.client.util.taints;

import static io.kubernetes.client.util.taints.Taints.taints;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import io.kubernetes.client.openapi.models.V1Node;
import org.junit.Test;

public class TaintsTest {
  @Test
  public void testAddTaints() {
    V1Node node = new V1Node();
    taints(node)
        .addTaint("key1", Taints.Effect.NO_SCHEDULE)
        .addTaint("key2", Taints.Effect.PREFER_NO_SCHEDULE);

    assertNotNull(Taints.findTaint(node, "key1", Taints.Effect.NO_SCHEDULE));
    assertNotNull(Taints.findTaint(node, "key2", Taints.Effect.PREFER_NO_SCHEDULE));
  }

  @Test
  public void testRemoveTaints() {
    V1Node node = new V1Node();
    taints(node)
        .addTaint("key1", Taints.Effect.NO_SCHEDULE)
        .addTaint("key1", Taints.Effect.PREFER_NO_SCHEDULE)
        .addTaint("key1", Taints.Effect.NO_EXECUTE)
        .addTaint("key2", Taints.Effect.PREFER_NO_SCHEDULE)
        .addTaint("key3", Taints.Effect.NO_SCHEDULE)
        .addTaint("key3", Taints.Effect.NO_EXECUTE);

    taints(node)
        // Remove all
        .removeTaint("key1")
        // Shouldn't remove, 'effect' is different.
        .removeTaint("key2", Taints.Effect.NO_SCHEDULE)
        // Remove matching
        .removeTaint("key3", Taints.Effect.NO_EXECUTE);

    assertNull(Taints.findTaint(node, "key1", Taints.Effect.NO_SCHEDULE));
    assertNull(Taints.findTaint(node, "key1", Taints.Effect.PREFER_NO_SCHEDULE));
    assertNull(Taints.findTaint(node, "key1", Taints.Effect.NO_EXECUTE));
    assertNull(Taints.findTaint(node, "key3", Taints.Effect.NO_EXECUTE));
    assertNotNull(Taints.findTaint(node, "key2", Taints.Effect.PREFER_NO_SCHEDULE));
    assertNotNull(Taints.findTaint(node, "key3", Taints.Effect.NO_SCHEDULE));
  }

  @Test
  public void testNoExecuteTaints(){
    V1Node node = new V1Node();
    String effect = "NoExecute";
    taints(node)
        .addTaint("key1", "value1", Taints.Effect.NO_EXECUTE);
    assertEquals(effect,Taints.findTaint(node, "key1", Taints.Effect.NO_EXECUTE).getEffect());
  }
}
