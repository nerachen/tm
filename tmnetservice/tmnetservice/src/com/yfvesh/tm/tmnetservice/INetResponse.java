package com.yfvesh.tm.tmnetservice;

import android.os.Bundle;

/* the interface for call-back for sync network function call */
public interface INetResponse {
	public void onNetResponse(int id, int orgreqtype,int status,int type, Bundle bundle);
}
