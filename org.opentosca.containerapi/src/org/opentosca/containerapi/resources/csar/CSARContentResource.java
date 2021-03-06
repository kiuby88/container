package org.opentosca.containerapi.resources.csar;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.opentosca.containerapi.osgi.servicegetter.FileRepositoryServiceHandler;
import org.opentosca.containerapi.resources.utilities.ResourceConstants;
import org.opentosca.containerapi.resources.utilities.Utilities;
import org.opentosca.containerapi.resources.xlink.Reference;
import org.opentosca.containerapi.resources.xlink.References;
import org.opentosca.containerapi.resources.xlink.XLinkConstants;
import org.opentosca.core.file.service.ICoreFileService;
import org.opentosca.core.model.artifact.directory.AbstractDirectory;
import org.opentosca.core.model.artifact.file.AbstractFile;
import org.opentosca.core.model.csar.CSARContent;
import org.opentosca.core.model.csar.id.CSARID;
import org.opentosca.exceptions.SystemException;
import org.opentosca.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource represents the root of a CSAR.
 * 
 * Copyright 2013 IAAS University of Stuttgart <br>
 * <br>
 * 
 * @author Markus Fischer - fischema@studi.informatik.uni-stuttgart.de
 * @author Rene Trefft - rene.trefft@developers.opentosca.org
 * 
 */
public class CSARContentResource {
	
	private static final Logger LOG = LoggerFactory.getLogger(CSARContentResource.class);
	// If the CSAR is null, CSAR does not exist in the Container
	private final CSARContent CSAR;
	
	
	public CSARContentResource(CSARContent csar) {
		this.CSAR = csar;
		CSARContentResource.LOG.debug("{} created: {}", this.getClass(), this);
		
		if (csar != null) {
			CSARContentResource.LOG.debug("Accessing content of CSAR \"{}\".", csar.getCSARID());
		} else {
			CSARContentResource.LOG.error("Requested CSAR is not stored!");
			System.out.println(csar);
		}
		
	}
	
	@GET
	@Produces(ResourceConstants.LINKED_XML)
	public Response getReferences(@Context UriInfo uriInfo) {
		
		if (this.CSAR == null) {
			CSARContentResource.LOG.info("CSAR is not stored.");
			
			return Response.status(Status.NOT_FOUND).build();
		}
		
		References refs = new References();
		
		Set<AbstractDirectory> directories = this.CSAR.getDirectories();
		for (AbstractDirectory directory : directories) {
			refs.getReference().add(new Reference(Utilities.buildURI(uriInfo.getAbsolutePath().toString(), directory.getName()), XLinkConstants.SIMPLE, directory.getName()));
		}
		
		Set<AbstractFile> files = this.CSAR.getFiles();
		for (AbstractFile file : files) {
			refs.getReference().add(new Reference(Utilities.buildURI(uriInfo.getAbsolutePath().toString(), file.getName()), XLinkConstants.SIMPLE, file.getName()));
		}
		
		refs.getReference().add(new Reference(uriInfo.getAbsolutePath().toString(), XLinkConstants.SIMPLE, XLinkConstants.SELF));
		
		return Response.ok(refs.getXMLString()).build();
		
	}
	
	@Path("{directoryOrFile}")
	public Object getDirectoryOrFile(@PathParam("directoryOrFile") String directoryOrFile) {
		
		CSARContentResource.LOG.debug("Checking if \"{}\" exists in CSAR \"{}\"...", directoryOrFile, this.CSAR.getCSARID());
		
		Set<AbstractDirectory> directories = this.CSAR.getDirectories();
		
		for (AbstractDirectory directory : directories) {
			if (directory.getName().equals(directoryOrFile)) {
				CSARContentResource.LOG.debug("\"{}\" is a directory of CSAR \"{}\".", directoryOrFile, this.CSAR.getCSARID());
				return new CSARDirectoryResource(directory, this.CSAR.getCSARID());
				
			}
		}
		
		Set<AbstractFile> files = this.CSAR.getFiles();
		
		for (AbstractFile file : files) {
			if (file.getName().equals(directoryOrFile)) {
				CSARContentResource.LOG.debug("\"{}\" is a file of CSAR \"{}\".", directoryOrFile, this.CSAR.getCSARID());
				return new CSARFileResource(file, this.CSAR.getCSARID());
			}
		}
		
		CSARContentResource.LOG.error("\"{}\" does not exist in CSAR \"{}\"!", directoryOrFile, this.CSAR.getCSARID());
		
		return null;
		
	}
	
	/**
	 * Moves this CSAR to the active / default storage provider if {@code move}
	 * is passed in {@code input} (body of a POST message).
	 * 
	 * @param input
	 * @return 200 (OK) - CSAR was moved successful.<br />
	 *         400 (bad request) - {@code move} was not passed.<br />
	 *         404 (not found) - CSAR is not stored.<br />
	 *         500 (internal server error) - moving CSAR failed.
	 * @throws SystemException
	 * @throws UserException
	 * 
	 * @see ICoreFileService#moveCSAR(CSARID)
	 */
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response moveCSAR(String input) throws UserException, SystemException {
		
		if (this.CSAR == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		if (input.equalsIgnoreCase("move")) {
			
			// try {
			
			FileRepositoryServiceHandler.getFileHandler().moveCSAR(this.CSAR.getCSARID());
			
			return Response.ok("Moving CSAR \"" + this.CSAR.getCSARID() + "\" was successful.").build();
			
			// } catch (UserException exc) {
			// CSARContentResource.LOG.warn("An User Exception occured.", exc);
			// } catch (SystemException exc) {
			// CSARContentResource.LOG.warn("A System Exception occured.", exc);
			// }
			
			// return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
		}
		
		return Response.status(Status.BAD_REQUEST).build();
		
	}
	
}
