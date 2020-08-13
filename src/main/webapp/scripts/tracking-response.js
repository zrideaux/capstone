/**
 * Initiate last call.
 */
function initiateLastCall() {
  window.lastCall = 0;
}

/**
 * Checks if this call is the latest call.
 *
 * @param call The call number of an rpc
 * @return a boolean stating this the response from an rpc is from the latest 
 *     rpc.
 */
function isLatestCall(call) {
  return window.lastCall == call;
}

/**
 * Used to create a call number to attach to an rpc.
 * Incriment the last call variable before a call number is created.
 *
 * @return the call number for an rpc.
 */
function getCall() {
  window.lastCall++;
  return window.lastCall;
}

export {
  getCall,
  initiateLastCall,
  isLatestCall
}