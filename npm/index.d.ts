declare module '@apiverve/zipdemographics' {
  export interface zipdemographicsOptions {
    api_key: string;
    secure?: boolean;
  }

  export interface zipdemographicsResponse {
    status: string;
    error: string | null;
    data: ZIPDemographicsData;
    code?: number;
  }


  interface ZIPDemographicsData {
      zip:        string;
      name:       string;
      acsYear:    number;
      population: Population;
      income:     Income;
      housing:    Housing;
      education:  Education;
      employment: Employment;
      race:       Race;
  }
  
  interface Education {
      collegeEducatedPct: number;
      bachelors:          number;
      masters:            number;
      professional:       number;
      doctorate:          number;
  }
  
  interface Employment {
      laborForce:       number;
      unemployed:       number;
      unemploymentRate: number;
  }
  
  interface Housing {
      medianHomeValue:   number;
      medianRent:        number;
      totalUnits:        number;
      occupiedUnits:     number;
      vacantUnits:       number;
      ownerOccupied:     number;
      renterOccupied:    number;
      homeOwnershipRate: number;
  }
  
  interface Income {
      medianHousehold: number;
      perCapita:       number;
  }
  
  interface Population {
      total:     number;
      male:      number;
      female:    number;
      medianAge: number;
  }
  
  interface Race {
      white:    Asian;
      black:    Asian;
      asian:    Asian;
      hispanic: Asian;
  }
  
  interface Asian {
      count:   number;
      percent: number;
  }

  export default class zipdemographicsWrapper {
    constructor(options: zipdemographicsOptions);

    execute(callback: (error: any, data: zipdemographicsResponse | null) => void): Promise<zipdemographicsResponse>;
    execute(query: Record<string, any>, callback: (error: any, data: zipdemographicsResponse | null) => void): Promise<zipdemographicsResponse>;
    execute(query?: Record<string, any>): Promise<zipdemographicsResponse>;
  }
}
