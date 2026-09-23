![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Coverage](https://img.shields.io/badge/coverage-92%25-green)
![Quality Gate](https://img.shields.io/badge/quality%20gate-passed-brightgreen)
![Security](https://img.shields.io/badge/security-no%20critical%20issues-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/license-Apache%202.0-blue)

# Tiara SMS Library

A lightweight Java library for integrating with the Tiara SMS Gateway to send sms.

Supports single sms, bulk sms, delivery receipt and MO processing and balance enquiry with minimal configuration.

## Features
- Single SMS sending 
- Bulk SMS sending
- Delivery Receipt Processing
- MO Processing
- Balance Enquiry
- Quick Configuration

## Getting Daraja Credentials
Create a developer account at [Tiara Connect Developer Portal](https://app.tiaraconnect.io/#/signup)

Documentation at [Tiara Connect Documentation](https://tiaraconnect.io/developers?section=intro)

## Installation
For Maven:
```xml
<dependency>
    <groupId>io.github.kathukyabrian</groupId>
    <artifactId>tiara</artifactId>
    <version>1.0.2</version>
</dependency>
```
Latest Version: 1.0.2

## Quick Start
### Configuration
- Create a file called `tiara.properties`
- Place the file on the application's classpath
```properties
single-sms-endpoint=https://api2.tiaraconnect.io/api/messaging/sendsms
bulk-sms-endpoint=https://api2.tiaraconnect.io/api/messaging/sendbatch
check-account-balance-endpoint=https://api2.tiaraconnect.io/api/messaging/checkbalance
api-key=api-key
sender-id=sender-id
read-timeout=
connect-timeout=
```

### Properties Description
| Property                       | Description                                                                                            |
|--------------------------------|--------------------------------------------------------------------------------------------------------|
| single-sms-endpoint            | The endpoint to send single sms on the Tiara SMS Gateway.                                              |
| bulk-sms-endpoint              | The endpoint to send bulk sms on the Tiara SMS Gateway.                                                |
| check-account-balance-endpoint | The endpoint to query sms units balance on the Tiara SMS Gateway.                                      |
| api-key                        | Provided by Tiara SMS gateway upon creation of an application. Used to authenticate all APIs.          |
| sender-id                      | The source address as configured on the Tiara SMS gateway.                                             |
| connect-timeout                | Time in seconds after which a connection attempt to the Tiara SMS gateway is dropped.                  |
| read-timeout                   | Time in seconds after which if a response has not come from the gateway, the calling application drops |

### Usage
- You are ready to use the library. It is a plug-and-play library.

#### Send Single SMS
- on your caller logic use the __sendSingle()__ method to send single SMS.
- This method will use the available configs.

```java
import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms")
    public ResponseEntity<SingleSMSResponse> sendSMS(@RequestBody Map<String, String> body) {
        SingleSMSResponse response = Tiara.sendSingle(body.get("to"), body.get("message"), UUID.randomUUID().toString());
        return ResponseEntity.ok(response);
    }
}
```

- there's an option that allows you to send an optional api key and  sender id. This option is available for applications with multiple configurations.
- it falls back to the supplied configuration if you pass null values to any of those configs.

```java
import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms")
    public ResponseEntity<SingleSMSResponse> sendSMS(@RequestBody Map<String, String> body) {
        SingleSMSResponse response = Tiara.sendSingle(body.get("to"), body.get("message"), UUID.randomUUID().toString(), "sender-id", "api-key");
        return ResponseEntity.ok(response);
    }
}
```

#### Send Bulk SMS
- on your caller logic use the __sendBulk()__ method to send single SMS.
- This method will use the available configs.
```java
package com.example.demo;

import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMS;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import io.github.kathukyabrian.dto.TiaraBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms/bulk")
    public ResponseEntity<List<SingleSMSResponse>> sendBulk(){
        List<SingleSMS> smsList = new ArrayList<>();
        SingleSMS one = new SingleSMS("0740272915", "Hello Brian");
        SingleSMS two = new SingleSMS("078786481", "Hello Brian");

        smsList.add(one);
        smsList.add(two);
        List<SingleSMSResponse> bulkResponse = Tiara.sendBulk(smsList, UUID.randomUUID().toString());
        return ResponseEntity.ok(Tiara.sendBulk(bulkResponse));
    }
}
```

- there's an option that allows you to send an optional api key and  sender id. This option is available for applications with multiple configurations.
- it falls back to the supplied configuration if you pass null values to any of those configs.
```java
package com.example.demo;

import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMS;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import io.github.kathukyabrian.dto.TiaraBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms/bulk")
    public ResponseEntity<List<SingleSMSResponse>> sendBulk(){
        List<SingleSMS> smsList = new ArrayList<>();
        SingleSMS one = new SingleSMS("0740272915", "Hello Brian");
        SingleSMS two = new SingleSMS("078786481", "Hello Brian");

        smsList.add(one);
        smsList.add(two);
        List<SingleSMSResponse> bulkResponse = Tiara.sendBulk(smsList, UUID.randomUUID().toString(), "sender-id", "api-key");
        return ResponseEntity.ok(Tiara.sendBulk(bulkResponse));
    }
}
```

#### Check Balance
- on your caller logic use the __getBalance()__ method to get balance for the current account(API Key owner).
```java
package com.example.demo;

import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import io.github.kathukyabrian.dto.TiaraBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/sms/balance")
    public ResponseEntity<TiaraBalanceResponse> getBalance() {
        TiaraBalanceResponse tiaraBalanceResponse = Tiara.getBalance();
        return ResponseEntity.ok(tiaraBalanceResponse);
    }
}
```

#### DLR Processing
- to process DLRs on your application, create an endpoint that takes __TiaraSMSCallback__ as the request body

```java
package com.example.demo;

import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.SingleSMS;
import io.github.kathukyabrian.dto.SingleSMSResponse;
import io.github.kathukyabrian.dto.TiaraBalanceResponse;
import io.github.kathukyabrian.dto.TiaraSMSCallback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms/dlr")
    public ResponseEntity<?> processDLR(@RequestBody TiaraSMSCallback dlrBody) {
        // set up logic to process dlr

        return ResponseEntity.ok(null);
    }
}
```

#### MO Processing
- to process MOs on your application, create an endpoint that takes __TiaraMORequest__ as the request body

```java
package com.example.demo;

import io.github.kathukyabrian.core.Tiara;
import io.github.kathukyabrian.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PostMapping("/sms/dlr")
    public ResponseEntity<?> processDLR(@RequestBody TiaraMORequest moRequest) {
        // set up logic to process MO

        return ResponseEntity.ok(null);
    }
}
```

## Contributing
Contributions are welcome.
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

## License
Apache 2.0