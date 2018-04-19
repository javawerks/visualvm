/*
 * Copyright (c) 2018, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.tools.visualvm.heapviewer.truffle.lang.python;

import com.sun.tools.visualvm.heapviewer.HeapContext;
import com.sun.tools.visualvm.heapviewer.model.HeapViewerNode;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleLocalObjectNode;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleObjectFieldNode;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.modules.profiler.heapwalk.details.api.DetailsSupport;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleObjectNode;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleObjectReferenceNode;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleOpenNodeActionProvider;
import com.sun.tools.visualvm.heapviewer.truffle.nodes.TruffleTypeNode;
import com.sun.tools.visualvm.heapviewer.ui.HeapViewerNodeAction;
import com.sun.tools.visualvm.heapviewer.ui.HeapViewerRenderer;
import java.util.Map;
import javax.swing.Icon;
import org.netbeans.lib.profiler.heap.FieldValue;
import org.netbeans.modules.profiler.api.icons.Icons;
import org.netbeans.modules.profiler.api.icons.LanguageIcons;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jiri Sedlacek
 */
@ServiceProvider(service=HeapViewerNodeAction.Provider.class)
public class PythonNodes extends TruffleOpenNodeActionProvider<PythonObject, PythonType, PythonHeapFragment, PythonLanguage> {
    
    @Override
    public boolean supportsView(HeapContext context, String viewID) {
        return PythonHeapFragment.isPythonHeap(context);
    }
    
    @Override
    protected boolean supportsNode(HeapViewerNode node) {
        return node instanceof PythonNodes.PythonNode;
    }

    @Override
    protected PythonLanguage getLanguage() {
        return PythonLanguage.instance();
    }
    
    
    static String getLogicalValue(PythonObject object, String type, Heap heap) {
        return DetailsSupport.getDetailsString(object.getInstance(), heap);
    }
    
    
    private static String computeName(TruffleObjectNode.InstanceBased<PythonObject> node, Heap heap) {
        String typeString = node.getTypeName();
        return typeString.substring(typeString.lastIndexOf('.') + 1) + "#" + node.getInstance().getInstanceNumber();
    }
    
    private static PythonObjectNode createCopy(TruffleObjectNode.InstanceBased<PythonObject> node) {
        return new PythonObjectNode(node.getTruffleObject(), node.getTypeName());
    }
    
    
    static interface PythonNode {}
    
    
    static class PythonObjectNode extends TruffleObjectNode.InstanceBased<PythonObject> implements PythonNode {
        
        public PythonObjectNode(PythonObject object, Heap heap) {
            this(object, object.getType(heap));
        }

        public PythonObjectNode(PythonObject robject, String type) {
            super(robject, type);
        }
        
        
        @Override
        protected String computeName(Heap heap) {
            return PythonNodes.computeName(this, heap);
        }
        
        protected String computeLogicalValue(PythonObject object, String type, Heap heap) {
            String logicalValue = PythonNodes.getLogicalValue(object, type, heap);
            return logicalValue != null ? logicalValue : super.computeLogicalValue(object, type, heap);
        }
        
        
        public PythonObjectNode createCopy() {
            PythonObjectNode copy = PythonNodes.createCopy(this);
            setupCopy(copy);
            return copy;
        }

        protected void setupCopy(PythonObjectNode copy) {
            super.setupCopy(copy);
        }
        
    }
    
    static class PythonLocalObjectNode extends TruffleLocalObjectNode.InstanceBased<PythonObject> implements PythonNode {
        
        PythonLocalObjectNode(PythonObject object, String type) {
            super(object, type);
        }
        
        @Override
        protected String computeName(Heap heap) {
            return PythonNodes.computeName(this, heap);
        }
        
        protected String computeLogicalValue(PythonObject object, String type, Heap heap) {
            String logicalValue = PythonNodes.getLogicalValue(object, type, heap);
            return logicalValue != null ? logicalValue : super.computeLogicalValue(object, type, heap);
        }
        
        
        public PythonObjectNode createCopy() {
            return PythonNodes.createCopy(this);
        }
        
    }
    
    static class PythonTypeNode extends TruffleTypeNode<PythonObject, PythonType> implements PythonNode {
        
        PythonTypeNode(PythonType type) {
            super(type);
        }

        @Override
        public HeapViewerNode createNode(PythonObject object, Heap heap) {
            String type = getType().getName();
            return new PythonNodes.PythonObjectNode(object, type);
        }

        @Override
        public TruffleTypeNode createCopy() {
            PythonTypeNode copy = new PythonTypeNode(getType());
            setupCopy(copy);
            return copy;
        }
        
        protected void setupCopy(PythonTypeNode copy) {
            super.setupCopy(copy);
        }
        
    }
    
    
    static class PythonObjectFieldNode extends TruffleObjectFieldNode.InstanceBased<PythonObject> implements PythonNode {
        
        PythonObjectFieldNode(PythonObject object, String type, FieldValue field) {
            super(object, type, field);
        }
        
        @Override
        protected String computeName(Heap heap) {
            return PythonNodes.computeName(this, heap);
        }
        
        protected String computeLogicalValue(PythonObject object, String type, Heap heap) {
            String logicalValue = PythonNodes.getLogicalValue(object, type, heap);
            return logicalValue != null ? logicalValue : super.computeLogicalValue(object, type, heap);
        }
        
        
        public PythonObjectNode createCopy() {
            return PythonNodes.createCopy(this);
        }
        
    }
    
    static class PythonObjectReferenceNode extends TruffleObjectReferenceNode.InstanceBased<PythonObject> implements PythonNode {
        
        PythonObjectReferenceNode(PythonObject object, String type, FieldValue value) {
            super(object, type, value);
        }
        
        @Override
        protected String computeName(Heap heap) {
            return PythonNodes.computeName(this, heap);
        }
        
        protected String computeLogicalValue(PythonObject object, String type, Heap heap) {
            String logicalValue = PythonNodes.getLogicalValue(object, type, heap);
            return logicalValue != null ? logicalValue : super.computeLogicalValue(object, type, heap);
        }
        
        
        public PythonObjectNode createCopy() {
            return PythonNodes.createCopy(this);
        }
        
    }
    
    static class PythonObjectAttributeReferenceNode extends TruffleObjectReferenceNode.InstanceBased<PythonObject> implements PythonNode {
        
        PythonObjectAttributeReferenceNode(PythonObject object, String type, FieldValue value) {
            super(object, type, value);
        }
        
        @Override
        protected String computeName(Heap heap) {
            return PythonNodes.computeName(this, heap);
        }
        
        protected String computeLogicalValue(PythonObject object, String type, Heap heap) {
            String logicalValue = PythonNodes.getLogicalValue(object, type, heap);
            return logicalValue != null ? logicalValue : super.computeLogicalValue(object, type, heap);
        }
        
        
        public PythonObjectNode createCopy() {
            return PythonNodes.createCopy(this);
        }
        
    }
    
    
    @ServiceProvider(service=HeapViewerRenderer.Provider.class)
    public static class PythonNodesRendererProvider extends HeapViewerRenderer.Provider {

        public boolean supportsView(HeapContext context, String viewID) {
            return true;
        }

        public void registerRenderers(Map<Class<? extends HeapViewerNode>, HeapViewerRenderer> renderers, HeapContext context) {
            PythonLanguage language = PythonLanguage.instance();
            Icon instanceIcon = language.createLanguageIcon(Icons.getIcon(LanguageIcons.INSTANCE));
            Icon packageIcon = language.createLanguageIcon(Icons.getIcon(LanguageIcons.PACKAGE));

            Heap heap = context.getFragment().getHeap();

            renderers.put(PythonNodes.PythonObjectNode.class, new TruffleObjectNode.Renderer(heap, instanceIcon));

            renderers.put(PythonNodes.PythonTypeNode.class, new TruffleTypeNode.Renderer(packageIcon));

            renderers.put(PythonNodes.PythonObjectFieldNode.class, new TruffleObjectFieldNode.Renderer(heap, instanceIcon));

            renderers.put(PythonNodes.PythonObjectReferenceNode.class, new TruffleObjectReferenceNode.Renderer(heap, instanceIcon));

            renderers.put(PythonNodes.PythonObjectAttributeReferenceNode.class, new TruffleObjectReferenceNode.Renderer(heap, instanceIcon, "attribute in"));
        }

    }
    
}