package org.opentosca.toscaengine.service.impl.resolver.data;

import org.opentosca.core.model.csar.CSARContent;
import org.opentosca.core.model.csar.id.CSARID;
import org.opentosca.model.tosca.Definitions;

/**
 * This DTO stores data of a CSAR and access to nested files as well as the
 * marshalled main Definitions. This DTO serves the transfer of the data between
 * the resolver classes of the package
 * org.opentosca.toscaengine.service.impl.resolver.
 * 
 * @author Christian Endres - endrescn@studi.informatik.uni-stuttgart.de
 * 
 */
public class DefinitionsData {
	
	private Definitions mainDef;
	private CSARContent csarContent;
	private CSARID csarID;
	
	
	public DefinitionsData(Definitions mainDef, CSARContent csarContent, CSARID csarID) {
		this.mainDef = mainDef;
		this.csarContent = csarContent;
		this.csarID = csarID;
	}
	
	/**
	 * @return the main Definitions
	 */
	public Definitions getMainDefinitions() {
		return this.mainDef;
	}
	
	/**
	 * @return the CSAR content
	 */
	public CSARContent getCSARContent() {
		return this.csarContent;
	}
	
	/**
	 * @return the CSAR ID
	 */
	public CSARID getCSARID() {
		return this.csarID;
	}
	
}
