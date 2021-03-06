package org.etcd4s

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}
import org.etcd4s.akkasupport.GrpcAkkaStreams.{getBidiFlow, getServerStreamingFLow}
import org.etcd4s.pb.etcdserverpb.LeaseGrpc.Lease
import org.etcd4s.pb.etcdserverpb.MaintenanceGrpc.Maintenance
import org.etcd4s.pb.etcdserverpb.WatchGrpc.Watch
import org.etcd4s.pb.etcdserverpb._

package object akkasupport {

  implicit class LeaseServiceWithAkkaSupport(leaseService: Lease) {
    val leaseKeepAliveFlow: Flow[LeaseKeepAliveRequest, LeaseKeepAliveResponse, NotUsed] =
      getBidiFlow(leaseService.leaseKeepAlive)
  }

  implicit class MaintenanceServiceWithAkkaSupport(maintenanceService: Maintenance) {
    def snapshotSource(request: SnapshotRequest): Source[SnapshotResponse, NotUsed] =
      getServerStreamingFLow(request)(maintenanceService.snapshot)
  }

  implicit class WatchServiceWithAkkaSupport(watchService: Watch) {
    val watchFlow: Flow[WatchRequest, WatchResponse, NotUsed] =
      getBidiFlow(watchService.watch)
  }

}
