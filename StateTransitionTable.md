# State Transition Table

This table shows all possible state transitions for the Order FSM.

| State | pay | prepare | markReady | deliver | cancel |
|-------|-----|---------|-----------|---------|--------|
| **NEW** | ✓ → PREPARING | ✗ | ✗ | ✗ | ✓ → CANCELLED |
| **PREPARING** | ✗ | ✗ | ✓ → READY | ✗ | ✓ → CANCELLED |
| **READY** | ✗ | ✗ | ✗ | ✓ → DELIVERED | ✗ |
| **DELIVERED** | ✗ | ✗ | ✗ | ✗ | ✗ |
| **CANCELLED** | ✗ | ✗ | ✗ | ✗ | ✗ |

## Legend
- ✓ = Transition allowed (shows target state if applicable)
- ✗ = Transition not allowed (prints error message, state unchanged)

## Notes
- **NEW**: Can only pay (moves to PREPARING) or cancel (moves to CANCELLED)
- **PREPARING**: Can mark ready (moves to READY) or cancel (moves to CANCELLED)
- **READY**: Can only deliver (moves to DELIVERED). Cannot cancel after ready.
- **DELIVERED**: Terminal state - all operations print "Completed" or "Already delivered"
- **CANCELLED**: Terminal state - all operations print "Cancelled" or "Already cancelled"

