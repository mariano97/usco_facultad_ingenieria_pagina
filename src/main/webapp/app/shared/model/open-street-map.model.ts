export interface IOpenStreetMap {
  place_id?: number;
  licence?: string;
  osm_type?: string;
  osm_id?: number;
  lat?: string;
  lon?: string;
  place_rank?: number;
  category?: string;
  type?: string;
  importance?: number;
  addresstype?: string;
  name?: string;
  display_name?: string;
  boundingbox?: string[];
  address?: IOpenStreetMapAddress;
}

export class OpenStreetMap implements IOpenStreetMap {
  constructor(
    public place_id?: number,
    public licence?: string,
    public osm_type?: string,
    public osm_id?: number,
    public lat?: string,
    public lon?: string,
    public place_rank?: number,
    public category?: string,
    public type?: string,
    public importance?: number,
    public addresstype?: string,
    public name?: string,
    public display_name?: string,
    public boundingbox?: string[] | null,
    public address?: IOpenStreetMapAddress | null
  ) {}
}

export interface IOpenStreetMapAddress {
  road?: string;
  village?: string;
  state_district?: string;
  state?: string;
  postcode?: string;
  country?: string;
  country_code?: string;
}

export class OpenStreetMapAddress implements IOpenStreetMapAddress {
  constructor(
    public road?: string,
    public village?: string,
    public state_district?: string,
    public state?: string,
    public postcode?: string,
    public country?: string,
    public country_code?: string
  ) {}
}
