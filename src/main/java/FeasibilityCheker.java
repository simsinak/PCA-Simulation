
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class FeasibilityCheker {
    public boolean checkWithInternalPolicies(Request r) {
        long startTime = System.currentTimeMillis();
        boolean policyMatched = true;
        String errMessage = "ERRORS IN REQUEST " + r.requestId + " :\n";
        String groupid = r.groupId;


        //retrive policies with this groupid and allGroups
        //retrive bandwidth
        HashMap<String, Object> tempHashMap = MapRepository.groupDeny.get(groupid);
        boolean exist = false;
        String bwtemp = null;
        double bwtempInt = 0;
        if (tempHashMap != null) {
            bwtemp = (String) tempHashMap.get("bandwidth");
            if (bwtemp != null) {
                bwtempInt = Double.parseDouble(bwtemp);
                exist = true;
            }
        }
        if (!exist) {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                bwtemp = (String) tempHashMap.get("bandwidth");
                if (bwtemp != null) {
                    bwtempInt = Double.parseDouble(bwtemp);
                    exist = true;
                }
            }
        }
        if (!exist) {
            Object o= MapRepository.allGroupsDeny.get("all").get("bandwidth");
            if (o != null) {
                bwtemp = (String)o;
                bwtempInt = Double.parseDouble(bwtemp);
                exist = true;
            }
        }
        if (!exist) {
            Object o= (String) MapRepository.allGroupsPermit.get("all").get("bandwidth");
            if (o != null) {
                bwtemp = (String)o;
                bwtempInt = Double.parseDouble(bwtemp);
                exist = true;
            }
        }
        //retrive locations permit
        Set<String> locationtempPermit = (Set<String>) MapRepository.allGroupsPermit.get("all").get("locations");
        if (locationtempPermit != null) {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                Object o=tempHashMap.get("locations");
                if (o!=null)
                locationtempPermit.addAll((Set<String>) o);
            }

        } else {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                Object o=tempHashMap.get("locations");
                if (o!=null)
                locationtempPermit = (Set<String>)o;
            }
        }
        //retrive locations deny
        Set<String> locationtempDeny = (Set<String>) MapRepository.allGroupsDeny.get("all").get("locations");
        if (locationtempDeny != null) {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                Object o=tempHashMap.get("locations");
                if (o!=null)
                locationtempDeny.addAll((Set<String>) o);
            }

        } else {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                Object o=tempHashMap.get("locations");
                if (o!=null)
                locationtempDeny = (Set<String>) o;
            }
        }

        //retrieve availability

        tempHashMap = MapRepository.groupDeny.get(groupid);
        exist = false;
        String avtemp = null;
        double avtempdouble = 0;
        if (tempHashMap != null) {
            avtemp = (String) tempHashMap.get("availability");
            if (avtemp != null) {
                avtempdouble = Double.parseDouble(avtemp);
                exist = true;
            }
        }
        if (!exist) {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                avtemp = (String) tempHashMap.get("availability");
                if (avtemp != null) {
                    avtempdouble = Double.parseDouble(avtemp);
                    exist = true;
                }
            }
        }
        if (!exist) {
            avtemp = (String) MapRepository.allGroupsDeny.get("all").get("availability");
            if (avtemp != null) {
                avtempdouble = Double.parseDouble(avtemp);
                exist = true;
            }
        }
        if (!exist) {
            avtemp = (String) MapRepository.allGroupsPermit.get("all").get("availability");
            if (avtemp != null) {
                avtempdouble = Double.parseDouble(avtemp);
                exist = true;
            }
        }

        //retrive services in locations permit
        HashMap<String, Set<String>> servicelocationtempPermit = (HashMap<String, Set<String>>) MapRepository.allGroupsPermit.get("all").get("servicesInLocation");
        if (servicelocationtempPermit != null) {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
            }

        } else {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                servicelocationtempPermit = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
            }
        }

        //retrive services in locations deny
        HashMap<String, Set<String>> servicelocationtempdeny = (HashMap<String, Set<String>>) MapRepository.allGroupsDeny.get("all").get("servicesInLocation");
        if (servicelocationtempdeny != null) {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
            }

        } else {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                servicelocationtempdeny = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
            }
        }


        //retrive services in all locations permit
        Set<String> servicetempPermit = (Set<String>) MapRepository.allGroupsPermit.get("all").get("services");
        if (servicetempPermit != null) {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                servicetempPermit.addAll((Set<String>) tempHashMap.get("services"));
            }

        } else {
            tempHashMap = MapRepository.groupPermit.get(groupid);
            if (tempHashMap != null) {
                servicetempPermit = (Set<String>) tempHashMap.get("services");
            }
        }

        //retrive services in all locations deny
        Set<String> servicetempDeny = (Set<String>) MapRepository.allGroupsDeny.get("all").get("services");
        if (servicetempDeny != null) {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                servicetempDeny.addAll((Set<String>) tempHashMap.get("services"));
            }

        } else {
            tempHashMap = MapRepository.groupDeny.get(groupid);
            if (tempHashMap != null) {
                servicetempDeny = (Set<String>) tempHashMap.get("services");
            }
        }

        /*fill this fields with retrieved Data after deny and permit union*/
        double bandwidth_max = bwtempInt;
        int bandwidth_min = 0;
        Set<String> locationspermit = null;
        Set<String> locationsdeny = null;
        if (locationtempPermit != null) {
            locationspermit = locationtempPermit;
        } else {
            locationspermit = new HashSet<String>();
        }
        if (locationtempDeny != null) {
            locationsdeny = locationtempDeny;
        } else {
            locationsdeny = new HashSet<String>();
        }


        HashMap<String, Set<String>> ServicesinLocationPermit = new HashMap<String, Set<String>>();
        HashMap<String, Set<String>> ServicesinLocationDeny = new HashMap<String, Set<String>>();
        if (servicelocationtempPermit != null) {
            ServicesinLocationPermit = servicelocationtempPermit;
        }
        if (servicelocationtempdeny != null) {
            ServicesinLocationDeny = servicelocationtempdeny;
        }
        Set<String> serviesinAllLocationsPermit = new HashSet<String>();
        Set<String> serviesinAllLocationsDeny = new HashSet<String>();
        if (servicetempPermit != null) {
            serviesinAllLocationsPermit = servicetempPermit;
        }
        if (servicetempDeny != null) {
            serviesinAllLocationsDeny = servicetempDeny;
        }

        HashMap<String, Set<String>> providersinLocation = new HashMap<String, Set<String>>();

        double availability = avtempdouble;
        /*retrive finished*/

        if (bandwidth_max != 0 && r.bandwidth > bandwidth_max) {
            policyMatched = false;
            errMessage += "√ bandwidth exceeds : " + (r.bandwidth - bandwidth_max) + "\n";
        }
        HashSet<String> tempServices = new HashSet<String>();
        HashSet<String> mainServices = new HashSet<String>();
        HashMap<String, String> vmTypes = new HashMap<String, String>();
        Set<String>  examined=new HashSet<String>();
        for (VMProperties p : r.properties.values()) {
            vmTypes.put(p.name, p.Type);
            Set<String> locationCopy = new HashSet<String>(p.location);
            Set<String> locationCopy2 = new HashSet<String>(p.location);
            locationCopy.removeAll(locationspermit);
            locationCopy2.retainAll(locationsdeny);
            if (!p.location.isEmpty() && ((!locationspermit.isEmpty() && locationCopy.size() != 0) || locationCopy2.size() != 0)) {
                policyMatched = false;
                if (!locationspermit.isEmpty() && locationCopy.size() != 0)
                    locationCopy2.addAll(locationCopy);
                errMessage += "√ locations not allowed : " + locationCopy2 + " for VM " + p.name + "\n";
            }
            tempServices.addAll(p.Services);
            mainServices.add(p.name);
            if (availability != 0 && p.QOS.contains("availability") && Double.parseDouble((String) p.QOS.get("availability")) > availability) {
                policyMatched = false;
                errMessage += "√ availability exceeds : " + (Double.parseDouble((String) p.QOS.get("availability")) - availability) + "\n";
            }
            Set<String> t = ServicesinLocationPermit.get(p.name);
            if (t != null && !t.isEmpty()) {
                //  Set<String> tcopy = new HashSet<String>(t);
                locationCopy = new HashSet<String>(p.location);
                locationCopy.removeAll(t);
                if (!p.location.isEmpty() && locationCopy.size() != 0) {
                    policyMatched = false;
                    errMessage += "√ service " + p.name + " not allowed in location : " + locationCopy + "\n";
                }
            }
            t = ServicesinLocationDeny.get(p.name);
            if (t != null) {
                //  Set<String> tcopy = new HashSet<String>(t);
                locationCopy = new HashSet<String>(p.location);
                locationCopy.retainAll(t);
                if (locationCopy.size() != 0) {
                    policyMatched = false;
                    errMessage += "√ service " + p.name + " not allowed in location : " + locationCopy + "\n";
                }
            }
            //new added for type
            if (!examined.contains(p.Type)){
                examined.add(p.Type);
            t = ServicesinLocationPermit.get(p.Type);
            if (t != null && !t.isEmpty()) {
                //  Set<String> tcopy = new HashSet<String>(t);
                locationCopy = new HashSet<String>(p.location);
                locationCopy.removeAll(t);
                if (!p.location.isEmpty() && locationCopy.size() != 0) {
                    policyMatched = false;
                    errMessage += "√ service " + p.Type + " not allowed in location : " + locationCopy + "\n";
                }
            }
            t = ServicesinLocationDeny.get(p.Type);
            if (t != null) {
                //  Set<String> tcopy = new HashSet<String>(t);
                locationCopy = new HashSet<String>(p.location);
                locationCopy.retainAll(t);
                if (locationCopy.size() != 0) {
                    policyMatched = false;
                    errMessage += "√ service " + p.Type + " not allowed in location : " + locationCopy + "\n";
                }
            }}
            //end

        }
        for (String service : tempServices) {

            if (!serviesinAllLocationsPermit.isEmpty() && !serviesinAllLocationsPermit.contains(service) && !serviesinAllLocationsPermit.contains(vmTypes.get(service))) {
                policyMatched = false;
                errMessage += "√ service " + service + " not allowed in All location : " + "\n";
            }


            if (serviesinAllLocationsDeny.contains(service) || serviesinAllLocationsDeny.contains(vmTypes.get(service))) {
                policyMatched = false;
                errMessage += "√ service " + service + " not allowed in All location : " + "\n";
            }

        }


        /////////////////////////////////////////////////////////
        examined.clear();
        for (String service : mainServices) {
            String id = service;
            //retrieve  r.properties.get(service).name ? type;

            //retrive locations permit
            locationtempPermit = (Set<String>) MapRepository.allServicesPermit.get("all").get("locations");
            if (locationtempPermit != null) {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    locationtempPermit.addAll((Set<String>) tempHashMap.get("locations"));
                    locationtempPermit.addAll((Set<String>) tempHashMap2.get("locations"));
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    locationtempPermit.addAll((Set<String>) tempHashMap2.get("locations"));
                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    locationtempPermit.addAll((Set<String>) tempHashMap.get("locations"));
                }

            } else {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    locationtempPermit = (Set<String>) tempHashMap.get("locations");
                    locationtempPermit.addAll((Set<String>) tempHashMap2.get("locations"));
                }else if(tempHashMap2 !=null && tempHashMap==null){
                    locationtempPermit = (Set<String>) tempHashMap2.get("locations");
                }else if(tempHashMap2 ==null && tempHashMap!=null){
                    locationtempPermit = (Set<String>) tempHashMap.get("locations");

                }
            }
            //retrive locations deny
            locationtempDeny = (Set<String>) MapRepository.allServicesDeny.get("all").get("locations");
            if (locationtempDeny != null) {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));

                if (tempHashMap != null && tempHashMap2!=null) {
                    locationtempDeny.addAll((Set<String>) tempHashMap.get("locations"));
                    locationtempDeny.addAll((Set<String>) tempHashMap2.get("locations"));

                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    locationtempDeny.addAll((Set<String>) tempHashMap.get("locations"));

                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    locationtempDeny.addAll((Set<String>) tempHashMap2.get("locations"));

                }

            } else {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    locationtempDeny = (Set<String>) tempHashMap.get("locations");
                    locationtempDeny.addAll((Set<String>) tempHashMap2.get("locations"));

                }
               else if (tempHashMap == null && tempHashMap2!=null) {
                    locationtempDeny = (Set<String>) tempHashMap2.get("locations");

                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    locationtempDeny = (Set<String>) tempHashMap.get("locations");

                }
            }
            //retrieve availability

            tempHashMap = MapRepository.serviceDeny.get(id);
            if(tempHashMap==null){
                tempHashMap = MapRepository.serviceDeny.get(vmTypes.get(id));
            }
            exist = false;
            avtemp = null;
            avtempdouble = 0;
            if (tempHashMap != null) {
                avtemp = (String) tempHashMap.get("availability");
                if (avtemp != null) {
                    avtempdouble = Double.parseDouble(avtemp);
                    exist = true;
                }
            }
            if (!exist) {
                tempHashMap = MapRepository.servicePermit.get(id);
                if(tempHashMap==null){
                    tempHashMap = MapRepository.servicePermit.get(vmTypes.get(id));
                }
                if (tempHashMap != null) {
                    avtemp = (String) tempHashMap.get("availability");
                    if (avtemp != null) {
                        avtempdouble = Double.parseDouble(avtemp);
                        exist = true;
                    }
                }
            }
            if (!exist) {
                avtemp = (String) MapRepository.allServicesDeny.get("all").get("availability");
                if (avtemp != null) {
                    avtempdouble = Double.parseDouble(avtemp);
                    exist = true;
                }
            }
            if (!exist) {
                avtemp = (String) MapRepository.allServicesPermit.get("all").get("availability");
                if (avtemp != null) {
                    avtempdouble = Double.parseDouble(avtemp);
                    exist = true;
                }
            }
            
            //retrive services in locations permit
            servicelocationtempPermit = (HashMap<String, Set<String>>) MapRepository.allServicesPermit.get("all").get("servicesInLocation");
            if (servicelocationtempPermit != null) {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2 !=null) {
                    servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
                    servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }
                else if (tempHashMap != null && tempHashMap2 ==null) {
                    servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
                }
                else if (tempHashMap == null && tempHashMap2 !=null) {
                    servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }

            } else {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2 !=null) {
                    servicelocationtempPermit = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
                    servicelocationtempPermit.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }
                else if (tempHashMap == null && tempHashMap2 !=null) {
                    servicelocationtempPermit = (HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation");
                }
                else if (tempHashMap != null && tempHashMap2 ==null) {
                    servicelocationtempPermit = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
                }
            }

            //retrive services in locations deny
            servicelocationtempdeny = (HashMap<String, Set<String>>) MapRepository.allServicesDeny.get("all").get("servicesInLocation");
            if (servicelocationtempdeny != null) {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));

                if (tempHashMap != null && tempHashMap2!=null) {
                    servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
                    servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation"));
                }

            } else {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    servicelocationtempdeny = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
                    servicelocationtempdeny.putAll((HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation"));
                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    servicelocationtempdeny = (HashMap<String, Set<String>>) tempHashMap.get("servicesInLocation");
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicelocationtempdeny = (HashMap<String, Set<String>>) tempHashMap2.get("servicesInLocation");
                }
            }


            //retrive services in all locations permit
            servicetempPermit = (Set<String>) MapRepository.allServicesPermit.get("all").get("services");
            if (servicetempPermit != null) {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    servicetempPermit.addAll((Set<String>) tempHashMap.get("services"));
                    servicetempPermit.addAll((Set<String>) tempHashMap2.get("services"));
                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    servicetempPermit.addAll((Set<String>) tempHashMap.get("services"));
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicetempPermit.addAll((Set<String>) tempHashMap2.get("services"));
                }

            } else {
                tempHashMap = MapRepository.servicePermit.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.servicePermit.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    servicetempPermit = (Set<String>) tempHashMap.get("services");
                    servicetempPermit.addAll((Set<String>) tempHashMap2.get("services"));
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicetempPermit = (Set<String>) tempHashMap2.get("services");
                }
                else if (tempHashMap != null && tempHashMap2==null) {
                    servicetempPermit = (Set<String>) tempHashMap.get("services");
                }
            }

            //retrive services in all locations deny
            servicetempDeny = (Set<String>) MapRepository.allServicesDeny.get("all").get("services");
            if (servicetempDeny != null) {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    servicetempDeny.addAll((Set<String>) tempHashMap.get("services"));
                    servicetempDeny.addAll((Set<String>) tempHashMap2.get("services"));
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicetempDeny.addAll((Set<String>) tempHashMap2.get("services"));
                }
               else  if (tempHashMap != null && tempHashMap2==null) {
                    servicetempDeny.addAll((Set<String>) tempHashMap.get("services"));
                }

            } else {
                tempHashMap = MapRepository.serviceDeny.get(id);
                HashMap<String , Object> tempHashMap2=MapRepository.serviceDeny.get(vmTypes.get(id));
                if (tempHashMap != null && tempHashMap2!=null) {
                    servicetempDeny = (Set<String>) tempHashMap.get("services");
                    servicetempDeny.addAll((Set<String>) tempHashMap2.get("services"));

                }
               else if (tempHashMap != null && tempHashMap2==null) {
                    servicetempDeny = (Set<String>) tempHashMap.get("services");
                }
                else if (tempHashMap == null && tempHashMap2!=null) {
                    servicetempDeny = (Set<String>) tempHashMap2.get("services");
                }
            }

                /*fill this fields with retrieved Data after deny and permit*/
            locationspermit = null;
            locationsdeny = null;
            if (locationtempPermit != null) {
                locationspermit = locationtempPermit;
            } else {
                locationspermit = new HashSet<String>();
            }
            if (locationtempDeny != null) {
                locationsdeny = locationtempDeny;
            } else {
                locationsdeny = new HashSet<String>();
            }

            ServicesinLocationPermit = new HashMap<String, Set<String>>();
            ServicesinLocationDeny = new HashMap<String, Set<String>>();
            if (servicelocationtempPermit != null) {
                ServicesinLocationPermit = servicelocationtempPermit;
            }
            if (servicelocationtempdeny != null) {
                ServicesinLocationDeny = servicelocationtempdeny;
            }
            serviesinAllLocationsPermit = new HashSet<String>();
            serviesinAllLocationsDeny = new HashSet<String>();
            if (servicetempPermit != null) {
                serviesinAllLocationsPermit = servicetempPermit;
            }
            if (servicetempDeny != null) {
                serviesinAllLocationsDeny = servicetempDeny;
            }
            providersinLocation = new HashMap<String, Set<String>>();
            availability = avtempdouble;
                /*retrive finished*/

            VMProperties temp = r.properties.get(service);
            Set<String> locationCopy = new HashSet<String>(temp.location);
            Set<String> locationCopy2 = new HashSet<String>(temp.location);
            locationCopy.removeAll(locationspermit);
            locationCopy2.retainAll(locationsdeny);
            if (!temp.location.isEmpty() && ((!locationspermit.isEmpty() && locationCopy.size() != 0) || locationCopy2.size() != 0)) {
                policyMatched = false;
                if (!locationspermit.isEmpty() && locationCopy.size() != 0)
                    locationCopy2.addAll(locationCopy);
                errMessage += "√ locations not allowed : " + locationCopy2 + " for service " + temp.name + "\n";
            }
            if (availability != 0 && temp.QOS.contains("availability") && Double.parseDouble((String) temp.QOS.get("availability")) > availability) {
                policyMatched = false;
                errMessage += "√ availability exceeds : " + (Double.parseDouble((String) temp.QOS.get("availability")) - availability) + "\n";
            }
            for (String services : temp.Services) {
                VMProperties vmpl=r.properties.get(services);
                String typeOfService;
                if (vmpl==null){
                    typeOfService=MapRepository.typeNames.get(services);
                }else{
                    typeOfService=vmpl.Type;
                }
                if (mainServices.contains(services)) {
                    Set<String> t = ServicesinLocationPermit.get(services);
                    Set<String> t2=ServicesinLocationPermit.get(vmpl.Type);
                    if (t != null && !t.isEmpty()) {
                        //  Set<String> tcopy = new HashSet<String>(t);
                        locationCopy = new HashSet<String>(vmpl.location);
                        locationCopy.removeAll(t);
                        if (locationCopy.size() != 0) {
                            policyMatched = false;
                            errMessage += "√ service " + service + "can't intract with " + services + " in location : " + locationCopy + "\n";
                        }
                    }
                    if (t2 != null && !t2.isEmpty()) {
                        //  Set<String> tcopy = new HashSet<String>(t);
                        locationCopy = new HashSet<String>(vmpl.location);
                        locationCopy.removeAll(t);
                        if (locationCopy.size() != 0) {
                            policyMatched = false;
                            errMessage += "√ service " + service + "can't intract with " + services + " in location : " + locationCopy + "\n";
                        }
                    }
                    t = ServicesinLocationDeny.get(services);
                    t2= ServicesinLocationDeny.get(vmpl.Type);
                    if (t != null) {
                        //  Set<String> tcopy = new HashSet<String>(t);
                        locationCopy = new HashSet<String>(vmpl.location);
                        locationCopy.retainAll(t);
                        if (locationCopy.size() != 0) {
                            policyMatched = false;
                            errMessage += "√ service " + service + "can't intract with " + services + " in location : " + locationCopy + "\n";
                        }
                    }
                    if (t2 != null) {
                        //  Set<String> tcopy = new HashSet<String>(t);
                        locationCopy = new HashSet<String>(vmpl.location);
                        locationCopy.retainAll(t);
                        if (locationCopy.size() != 0) {
                            policyMatched = false;
                            errMessage += "√ service " + service + "can't intract with " + services + " in location : " + locationCopy + "\n";
                        }
                    }
                }
                if (!serviesinAllLocationsPermit.isEmpty() && (!serviesinAllLocationsPermit.contains(services) && !serviesinAllLocationsPermit.contains(typeOfService))) {
                    policyMatched = false;
                    errMessage += "√ service " + service + "can't intract with " + services + " in All locations " + "\n";
                }


                if (serviesinAllLocationsDeny.contains(services) || serviesinAllLocationsDeny.contains(typeOfService)) {
                    policyMatched = false;
                    errMessage += "√ service " + service + "can't intract with " + services + " in All locations " + "\n";
                }
                //retrieve service and allservices
                //retrive bandwidth
                //can said reverse with id
                tempHashMap = MapRepository.serviceDeny.get(services);
                exist = false;
                bwtemp = null;
                bwtempInt = 0;
                if (tempHashMap != null) {
                    bwtemp = (String) tempHashMap.get("bandwidth");
                    if (bwtemp != null) {
                        bwtempInt = Double.parseDouble(bwtemp);
                        exist = true;
                    }
                }
                if (!exist) {
                    tempHashMap = MapRepository.servicePermit.get(services);
                    if (tempHashMap != null) {
                        bwtemp = (String) tempHashMap.get("bandwidth");
                        if (bwtemp != null) {
                            bwtempInt = Double.parseDouble(bwtemp);
                            exist = true;
                        }
                    }
                }
                if(!exist){
                tempHashMap = MapRepository.serviceDeny.get(typeOfService);
                if (tempHashMap != null) {
                    bwtemp = (String) tempHashMap.get("bandwidth");
                    if (bwtemp != null) {
                        bwtempInt = Double.parseDouble(bwtemp);
                        exist = true;
                    }
                }
                if (!exist) {
                    tempHashMap = MapRepository.servicePermit.get(services);
                    if (tempHashMap != null) {
                        bwtemp = (String) tempHashMap.get("bandwidth");
                        if (bwtemp != null) {
                            bwtempInt = Double.parseDouble(bwtemp);
                            exist = true;
                        }
                    }
                }}
                if (!exist) {
                    bwtemp = (String) MapRepository.allServicesDeny.get("all").get("bandwidth");
                    if (bwtemp != null) {
                        bwtempInt = Double.parseDouble(bwtemp);
                        exist = true;
                    }
                }
                if (!exist) {
                    bwtemp = (String) MapRepository.allServicesPermit.get("all").get("bandwidth");
                    if (bwtemp != null) {
                        bwtempInt = Double.parseDouble(bwtemp);
                        exist = true;
                    }
                }
                /*fill this fields with retrieved Data after deny and permit*/
                bandwidth_max = bwtempInt;
                bandwidth_min = 0;

                /*retrive finished*/

                if (bandwidth_max != 0 && temp.bandwidth.containsKey(services) && temp.bandwidth.get(services) > bandwidth_max) {
                    policyMatched = false;
                    errMessage += "√ bandwidth exceeds : " + (temp.bandwidth.get(services) - bandwidth_max) + "\n";
                }
            }

            //bw of services


        }
        /////////////////////////////////////////////////////////////////
        //              compare with preRequests                        //
        /////////////////////////////////////////////////////////////////
        //??????
        try {
            test.bw.write("FEASIBILITY CHECKED FOR REQUEST " + r.requestId + " IN " + (System.currentTimeMillis() - startTime) + " Milis\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!policyMatched) {
            try {
                test.bw.write(errMessage+"\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return policyMatched;
    }
}
