import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sinaaskarnejad on 3/23/16.
 */
//this class get a policy, parce it and save it in profile
public class OrganizationPolicyParser {
    public void init(){
        MapRepository.allGroupsDeny.put("all" , new HashMap<String, Object>());
        MapRepository.allServicesDeny.put("all" , new HashMap<String, Object>());
        MapRepository.allGroupsPermit.put("all" , new HashMap<String, Object>());
        MapRepository.allServicesPermit.put("all" , new HashMap<String, Object>());
    }

    public boolean parse(String policy){
        if (policy.startsWith("group")){
            return parseGroup(policy.substring(6));

        }else if(policy.startsWith("service")){
            return parseService(policy.substring(8));

        }else if(policy.contains("allGroups")){
            return parseAllGroups(policy.substring(10));

        }else if(policy.contains("allServices")){
           return  parseAllServices(policy.substring(12));

        }else{
            System.out.println("Format not supported yet");
            return false;
        }
    }
    private boolean parseGroup(String policy){
        String[] tokens=policy.split(" +");
        String id;
            id=tokens[0];
            String regex= "\\s+";
            Pattern p=Pattern.compile(regex);
            Matcher m=p.matcher(tokens[0]);
            if(m.find()){
                System.out.println("Too musch space after First Word");
                return false;
            }

        if(tokens[2].equals("bandwidth")){
            //write id , tokern[1] , token[2] , token[3]
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                if(temp==null){
                   temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.groupPermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.groupDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[2].equals("locations")){
            // write id , token[1] , token[2] , token[3]...
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[2]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 3; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[2] , set);
                MapRepository.groupPermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[2]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 3; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[2] , set);
                MapRepository.groupDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[2].equals("services")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                    //write id , token[1] , token[2] , token[3].. in locations[1]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.groupPermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.groupDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // write id , token[1] , token[2] , token[3]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.groupPermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.groupDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }

        }else if(tokens[2].equals("providers")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                //write id , token[1] , token[2] , token[3].. in locations[1]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.groupPermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.groupDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // write id , token[1] , token[2] , token[3]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.groupPermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.groupDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }
        }else if(tokens[2].equals("availability")){
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.groupPermit.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.groupPermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.groupDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.groupDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else{
            return false;
        }
        return true;
    }
    private boolean parseService(String policy){
        String[] tokens=policy.split(" +");
        String id;
        id = tokens[0];
        String regex= "\\s+";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(tokens[0]);
        if(m.find()){
            System.out.println("Too musch space after First Word");
            return false;
        }
        if(tokens[2].equals("bandwidth")){
            //write id , tokern[1] , token[2] , token[3]
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.servicePermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.serviceDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[2].equals("locations")){
            // write id , token[1] , token[2] , token[3]...
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[2]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 3; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[2] , set);
                MapRepository.servicePermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[2]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 3; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[2] , set);
                MapRepository.serviceDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[2].equals("services")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                //write id , token[1] , token[2] , token[3].. in locations[1]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.servicePermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.serviceDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // write id , token[1] , token[2] , token[3]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.servicePermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.serviceDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }



        }else if(tokens[2].equals("providers")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                //write id , token[1] , token[2] , token[3].. in locations[1]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.servicePermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.serviceDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // write id , token[1] , token[2] , token[3]...
                if (tokens[1].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.servicePermit.put(id , temp);
                }
                else if (tokens[1].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[2]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 3; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[2] , set);
                    MapRepository.serviceDeny.put(id , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }
        }else if(tokens[2].equals("availability")){
            if (tokens[1].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.servicePermit.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.servicePermit.put(id , temp);
            }
            else if (tokens[1].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.serviceDeny.get(id);
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[2] , tokens[3]);
                MapRepository.serviceDeny.put(id , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else{
            return false;
        }
        return true;
    }
    private boolean parseAllGroups(String policy){
        String[] tokens=policy.split(" +");
        if(tokens[1].equals("bandwidth")){
            //tokern[0], tokern[1] , token[2]
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allGroupsPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allGroupsDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[1].equals("locations")){
            //tokern[0], tokern[1] , token[2]...
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[1]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 2; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[1] , set);
                MapRepository.allGroupsPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[1]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 2; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[1] , set);
                MapRepository.allGroupsDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[1].equals("services")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                // token[0] , token[1] , token[2].. in locations[1]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.allGroupsPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.allGroupsDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // token[0] , token[1] , token[2]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allGroupsPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allGroupsDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }

        }else if(tokens[1].equals("providers")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                //write id , token[1] , token[2] , token[3].. in locations[1]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.allGroupsPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.allGroupsDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // write id , token[1] , token[2] , token[3]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allGroupsPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allGroupsDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }
        }else if(tokens[1].equals("availability")){
            // tokern[0] , token[1] , token[2]...
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allGroupsPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allGroupsPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allGroupsDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allGroupsDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else{
            return false;
        }
        return true;
    }
    private boolean parseAllServices(String policy) {
        String[] tokens=policy.split(" +");
        if(tokens[1].equals("bandwidth")){
            //tokern[0], tokern[1] , token[2]
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allServicesPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allServicesDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[1].equals("locations")){
            //tokern[0], tokern[1] , token[2]...
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[1]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 2; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[1] , set);
                MapRepository.allServicesPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                Object o=temp.get(tokens[1]);
                if(o==null){
                    o=new HashSet<String>();
                }
                Set<String> set=(HashSet<String>)o;
                for (int i = 2; i < tokens.length ; i++) {
                    set.add(tokens[i]);
                }
                temp.put(tokens[1] , set);
                MapRepository.allServicesDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else if(tokens[1].equals("services")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                // token[0] , token[1] , token[2].. in locations[1]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.allServicesPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("servicesInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("servicesInLocation" , hashmap);
                    MapRepository.allServicesDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                //  token[0] , token[1] , token[2]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allServicesPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allServicesDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }

        }else if(tokens[1].equals("providers")){
            int position;
            if ((position=policy.indexOf("location"))!=-1){
                String[] locations=policy.substring(position).split(" +");
                List<String> locationsList=Arrays.asList(locations);
                locationsList.remove(0);
                tokens=policy.substring(0,position).split(" +");
                // token[0] , token[1] , token[2].. in locations[1]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.allServicesPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get("providersInLocation");
                    if(o==null){
                        o=new HashMap<String , Set<String>>();
                    }
                    HashMap<String , Set<String>> hashmap=(HashMap<String , Set<String>>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        Set<String> set=hashmap.get(tokens[i]);
                        if(set==null){
                            set =new HashSet<String>();
                        }
                        set.addAll(locationsList);
                        hashmap.put(tokens[i],set);
                    }
                    temp.put("providersInLocation" , hashmap);
                    MapRepository.allServicesDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done

            }else {
                // token[0] , token[1] , token[2]...
                if (tokens[0].toLowerCase().equals("permit")){
                    HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allServicesPermit.put("all" , temp);
                }
                else if (tokens[0].toLowerCase().equals("deny")){
                    HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                    if(temp==null){
                        temp=new HashMap<String, Object>();
                    }
                    Object o=temp.get(tokens[1]);
                    if(o==null){
                        o=new HashSet<String>();
                    }
                    Set<String> set=(HashSet<String>)o;
                    for (int i = 2; i < tokens.length ; i++) {
                        set.add(tokens[i]);
                    }
                    temp.put(tokens[1] , set);
                    MapRepository.allServicesDeny.put("all" , temp);
                }
                else {
                    System.out.println("PERMIT or DENY keyword didn't found");
                    return false;
                }
                //write Done
            }
        }else if(tokens[1].equals("availability")){
            // tokern[0] , token[1] , token[2]...
            if (tokens[0].toLowerCase().equals("permit")){
                HashMap<String , Object> temp=MapRepository.allServicesPermit.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allServicesPermit.put("all" , temp);
            }
            else if (tokens[0].toLowerCase().equals("deny")){
                HashMap<String , Object> temp=MapRepository.allServicesDeny.get("all");
                if(temp==null){
                    temp=new HashMap<String, Object>();
                }
                temp.put(tokens[1] , tokens[2]);
                MapRepository.allServicesDeny.put("all" , temp);
            }
            else {
                System.out.println("PERMIT or DENY keyword didn't found");
                return false;
            }
            //write Done
        }else{
            return false;
        }
        return true;
    }

}
