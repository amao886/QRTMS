package com.ycg.ksh.common.extend.lock;

import java.io.Serializable;

public interface DistributedSynchronize {

    void initialize(String _subpath) throws Exception;

	boolean lock(String path, Serializable source);

    void unlock(String path, Serializable source);

}
