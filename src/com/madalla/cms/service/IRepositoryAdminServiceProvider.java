package com.madalla.cms.service;

/**
 * This should be implemented by the class supplying the implentation
 * of the IRepositoryAdminService
 * <p>
 * @author Eugene Malan
 * @see IRepositoryAdminService
 */
public interface IRepositoryAdminServiceProvider {

    IRepositoryAdminService getRepositoryAdminService();
}
