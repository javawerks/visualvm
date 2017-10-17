/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * 
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.tools.visualvm.truffle.heapwalker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.netbeans.lib.profiler.heap.Instance;

/**
 *
 * @author Jiri Sedlacek
 */
public abstract class AbstractObjectsProvider {
    
    protected static Set<Instance> getDominatorRoots(List<Instance> searchInstances) {
        Set<Instance> dominators = new HashSet(searchInstances);
        Set<Instance> removed = new HashSet();

        for (Instance instance : searchInstances) {
            if (dominators.contains(instance)) {
                Instance dom = instance;
                long retainedSize = instance.getRetainedSize();

                while (!instance.isGCRoot()) {
                    instance = instance.getNearestGCRootPointer();
                    if (dominators.contains(instance) && instance.getRetainedSize()>=retainedSize) {
                        dominators.remove(dom);
                        removed.add(dom);
                        dom = instance;
                        retainedSize = instance.getRetainedSize();
                    }
                    if (removed.contains(instance)) {
                        dominators.remove(dom);
                        removed.add(dom);
                        break;
                    }
                }
            }
        }
        return dominators;
    }
    
}