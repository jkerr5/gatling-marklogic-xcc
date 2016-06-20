package org.nuxeo.gatling.marklogic

import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.config.Protocols

abstract class MarkLogicActionBuilder extends ActionBuilder {

  def xccProtocol(protocols: Protocols) =
    protocols.getProtocol[XccMarkLogicProtocol]
      .getOrElse(throw new UnsupportedOperationException("XCCMarkLogicProtocol Protocol wasn't registered"))

}
