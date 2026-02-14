# ZIP Demographics API

ZIP Demographics provides detailed demographic data for any US ZIP code including population, income, education, housing, employment, and racial composition from the US Census American Community Survey.

![Build Status](https://img.shields.io/badge/build-passing-green)
![Code Climate](https://img.shields.io/badge/maintainability-B-purple)
![Prod Ready](https://img.shields.io/badge/production-ready-blue)
[![npm version](https://img.shields.io/npm/v/@apiverve/zipdemographics.svg)](https://www.npmjs.com/package/@apiverve/zipdemographics)

This is a Javascript Wrapper for the [ZIP Demographics API](https://apiverve.com/marketplace/zipdemographics?utm_source=npm&utm_medium=readme)

---

## Installation

Using npm:
```shell
npm install @apiverve/zipdemographics
```

Using yarn:
```shell
yarn add @apiverve/zipdemographics
```

---

## Configuration

Before using the ZIP Demographics API client, you have to setup your account and obtain your API Key.
You can get it by signing up at [https://apiverve.com](https://apiverve.com?utm_source=npm&utm_medium=readme)

---

## Quick Start

[Get started with the Quick Start Guide](https://docs.apiverve.com/quickstart?utm_source=npm&utm_medium=readme)

The ZIP Demographics API documentation is found here: [https://docs.apiverve.com/ref/zipdemographics](https://docs.apiverve.com/ref/zipdemographics?utm_source=npm&utm_medium=readme).
You can find parameters, example responses, and status codes documented here.

### Setup

```javascript
const zipdemographicsAPI = require('@apiverve/zipdemographics');
const api = new zipdemographicsAPI({
    api_key: '[YOUR_API_KEY]'
});
```

---

## Usage

---

### Perform Request

Using the API is simple. All you have to do is make a request. The API will return a response with the data you requested.

```javascript
var query = {
  zip: "90210"
};

api.execute(query, function (error, data) {
    if (error) {
        return console.error(error);
    } else {
        console.log(data);
    }
});
```

---

### Using Promises

You can also use promises to make requests. The API returns a promise that you can use to handle the response.

```javascript
var query = {
  zip: "90210"
};

api.execute(query)
    .then(data => {
        console.log(data);
    })
    .catch(error => {
        console.error(error);
    });
```

---

### Using Async/Await

You can also use async/await to make requests. The API returns a promise that you can use to handle the response.

```javascript
async function makeRequest() {
    var query = {
  zip: "90210"
};

    try {
        const data = await api.execute(query);
        console.log(data);
    } catch (error) {
        console.error(error);
    }
}
```

---

## Example Response

```json
{
  "status": "ok",
  "error": null,
  "data": {
    "zip": "90210",
    "name": "ZCTA5 90210",
    "acsYear": 2022,
    "population": {
      "total": 21741,
      "male": 10234,
      "female": 11507,
      "medianAge": 45.3
    },
    "income": {
      "medianHousehold": 153891,
      "perCapita": 98234
    },
    "housing": {
      "medianHomeValue": 2875000,
      "medianRent": 3250,
      "totalUnits": 10234,
      "occupiedUnits": 8976,
      "vacantUnits": 1258,
      "ownerOccupied": 5678,
      "renterOccupied": 3298,
      "homeOwnershipRate": 63.2
    },
    "education": {
      "collegeEducatedPct": 72.4,
      "bachelors": 4523,
      "masters": 2341,
      "professional": 1234,
      "doctorate": 567
    },
    "employment": {
      "laborForce": 11234,
      "unemployed": 423,
      "unemploymentRate": 3.8
    },
    "race": {
      "white": {
        "count": 16892,
        "percent": 77.7
      },
      "black": {
        "count": 542,
        "percent": 2.5
      },
      "asian": {
        "count": 2345,
        "percent": 10.8
      },
      "hispanic": {
        "count": 1423,
        "percent": 6.5
      }
    }
  }
}
```

---

## Customer Support

Need any assistance? [Get in touch with Customer Support](https://apiverve.com/contact?utm_source=npm&utm_medium=readme).

---

## Updates

Stay up to date by following [@apiverveHQ](https://twitter.com/apiverveHQ) on Twitter.

---

## Legal

All usage of the APIVerve website, API, and services is subject to the [APIVerve Terms of Service](https://apiverve.com/terms?utm_source=npm&utm_medium=readme), [Privacy Policy](https://apiverve.com/privacy?utm_source=npm&utm_medium=readme), and [Refund Policy](https://apiverve.com/refund?utm_source=npm&utm_medium=readme).

---

## License
Licensed under the The MIT License (MIT)

Copyright (&copy;) 2026 APIVerve, and EvlarSoft LLC

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
