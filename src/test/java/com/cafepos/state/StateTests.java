package com.cafepos.state;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StateTests {
    
    @Test
    void order_fsm_happy_path() {
        OrderFSM fsm = new OrderFSM();
        
        assertEquals("NEW", fsm.status());
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.markReady();
        assertEquals("READY", fsm.status());
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }
    
    @Test
    void cannot_prepare_before_pay() {
        OrderFSM fsm = new OrderFSM();
        
        assertEquals("NEW", fsm.status());
        fsm.prepare(); // should not change state
        assertEquals("NEW", fsm.status());
    }
    
    @Test
    void cannot_deliver_before_ready() {
        OrderFSM fsm = new OrderFSM();
        
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.deliver(); // should not change state
        assertEquals("PREPARING", fsm.status());
    }
    
    @Test
    void can_cancel_from_new() {
        OrderFSM fsm = new OrderFSM();
        
        assertEquals("NEW", fsm.status());
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }
    
    @Test
    void can_cancel_from_preparing() {
        OrderFSM fsm = new OrderFSM();
        
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }
    
    @Test
    void cannot_cancel_after_ready() {
        OrderFSM fsm = new OrderFSM();
        
        fsm.pay();
        fsm.markReady();
        assertEquals("READY", fsm.status());
        fsm.cancel(); // should not change state
        assertEquals("READY", fsm.status());
    }
    
    @Test
    void delivered_state_is_terminal() {
        OrderFSM fsm = new OrderFSM();
        
        fsm.pay();
        fsm.markReady();
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
        
        // Try various operations - should stay in DELIVERED
        fsm.pay();
        assertEquals("DELIVERED", fsm.status());
        fsm.prepare();
        assertEquals("DELIVERED", fsm.status());
        fsm.markReady();
        assertEquals("DELIVERED", fsm.status());
    }
    
    @Test
    void cancelled_state_is_terminal() {
        OrderFSM fsm = new OrderFSM();
        
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
        
        // Try various operations - should stay in CANCELLED
        fsm.pay();
        assertEquals("CANCELLED", fsm.status());
        fsm.prepare();
        assertEquals("CANCELLED", fsm.status());
        fsm.markReady();
        assertEquals("CANCELLED", fsm.status());
    }
}

