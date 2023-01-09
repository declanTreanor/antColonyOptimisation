package com.eon.ants.concurrrency;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
/**
 * A singleton instance of this will ensure
 * consistent data between objects
 */
public  class ACOLockObject extends ReentrantReadWriteLock{

}
