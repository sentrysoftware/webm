/*
  (C) Copyright IBM Corp. 2006, 2009

  THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.

  You can obtain a current copy of the Eclipse Public License from
  http://www.opensource.org/licenses/eclipse-1.0.php

  @author : Endre Bak, ebak@de.ibm.com
 * 
 * Flag       Date        Prog         Description
 * -------------------------------------------------------------------------------
 * 1663270    2007-02-19  ebak         Minor performance problems
 * 2003590    2008-06-30  blaschke-oss Change licensing from CPL to EPL
 * 2524131    2009-01-21  raman_arora  Upgrade client to JDK 1.5 (Phase 1)
 * 2531371    2009-02-10  raman_arora  Upgrade client to JDK 1.5 (Phase 2) 
 */

package org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax;

/*-
 * ╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲
 * WbeM Java Client
 * ჻჻჻჻჻჻
 * Copyright (C) 2023 Sentry Software
 * ჻჻჻჻჻჻
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * ╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱╲╱
 */

import java.util.HashMap;

import org.sentrysoftware.wbem.sblim.cimclient.internal.cimxml.sax.node.Node;

/**
 * NodePool is used by XMLDefaultHandlerImpl to store unused Node instances
 * which can be reused later without instantiating new ones.
 */
public class NodePool {

	private HashMap<String, PoolStack> iPoolMap = new HashMap<String, PoolStack>(512);

	private int iHitCnt = 0;

	private int iMissCnt = 0;

	/**
	 * addNode
	 * 
	 * @param pNode
	 */
	public void addNode(Node pNode) {
		PoolStack ps = this.iPoolMap.get(pNode.getNodeName());
		if (ps == null) {
			this.iPoolMap.put(pNode.getNodeName(), new PoolStack(pNode));
			return;
		}
		ps.put(pNode);
	}

	/**
	 * getNode
	 * 
	 * @param pNodeName
	 * @return Node
	 */
	public Node getNode(String pNodeName) {
		PoolStack ps = this.iPoolMap.get(pNodeName);
		if (ps == null) {
			++this.iMissCnt;
			return null;
		}
		Node node = ps.get();
		if (node == null) ++this.iMissCnt;
		else ++this.iHitCnt;
		return node;
	}

	/**
	 * getHitCnt
	 * 
	 * @return int
	 */
	public int getHitCnt() {
		return this.iHitCnt;
	}

	/**
	 * getMissCnt
	 * 
	 * @return int
	 */
	public int getMissCnt() {
		return this.iMissCnt;
	}

}

class PoolStack {

	private static final int CAPACITY = 8, MAX_USECNT = CAPACITY - 1, MAX_IDX = MAX_USECNT;

	private Node[] iNodeA = new Node[CAPACITY];

	private int iIdx = 0, iUseCnt = 0;

	/**
	 * Ctor.
	 * 
	 * @param pNode
	 */
	public PoolStack(Node pNode) {
		put(pNode);
	}

	/**
	 * put
	 * 
	 * @param pNode
	 */
	public void put(Node pNode) {
		if (this.iUseCnt < MAX_USECNT) ++this.iUseCnt;
		this.iNodeA[this.iIdx] = pNode;
		incIdx();
	}

	/**
	 * get
	 * 
	 * @return Node
	 */
	public Node get() {
		if (this.iUseCnt == 0) return null;
		--this.iUseCnt;
		decIdx();
		return this.iNodeA[this.iIdx];
	}

	private void decIdx() {
		this.iIdx = (this.iIdx == 0 ? MAX_IDX : this.iIdx - 1);
	}

	private void incIdx() {
		this.iIdx = (this.iIdx == MAX_IDX ? 0 : this.iIdx + 1);
	}

}
