package com.eon.ants.concurrrency;


import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A singleton instance of this will ensure consistent data between objects
 */
@Primary
@Component
public class ACOLockObjectPheremones extends ReentrantReadWriteLock {

}
