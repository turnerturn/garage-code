

//on spot msg:  send each upload message.  Cache correlation id to confirm replies to each upload.
//on polling:  start polling and wait for jms-listener.
//on download: map<correlationId, variable>  when reply is received: get mapped variable and put new value
//on done:  wait for all downloads before replying to spot.
//on abort/manual_entry: reply to spot
//on page:  download current page.  Set to 1 if < 1 or > max. Refresh uploads with current page.