package thu.cs.lyw.rm.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2Test
{
	static AmazonEC2 ec2;
	private static void init() throws Exception{
		AWSCredentials myCredential = new PropertiesCredentials(new File("provider_ec2.properties"));
		ec2 = new AmazonEC2Client(myCredential);
		ec2.setEndpoint("ec2.ap-southeast-1.amazonaws.com");
		try{
			CreateSecurityGroupRequest createSecurityGroupRequest = 
					new CreateSecurityGroupRequest();
			createSecurityGroupRequest.withGroupName("RMSecurityGroup")
					.withDescription("Resource Manager Security Group");
			ec2.createSecurityGroup(createSecurityGroupRequest);
		}catch(AmazonServiceException ase){
			System.out.println("Caught Exception: " + ase.getMessage());
		}
		try{
			IpPermission ipPermission = new IpPermission();
			ipPermission.withIpRanges("166.111.0.0/16", "166.111.0.0/16")
				.withIpProtocol("tcp")
				.withFromPort(22)
				.withToPort(22);
			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
					new AuthorizeSecurityGroupIngressRequest();
			authorizeSecurityGroupIngressRequest.withGroupName("RMSecurityGroup")
				.withIpPermissions(ipPermission);
			ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
		}catch(AmazonServiceException ase){
			System.out.println("Caught Exception: " + ase.getMessage());
		}
		try{
			CreateKeyPairRequest createKeyPairRequest = 
					new CreateKeyPairRequest();
			createKeyPairRequest.withKeyName("RM.EC2.Test");
			ec2.createKeyPair(createKeyPairRequest);
		}catch(AmazonServiceException ase){
			System.out.println("Caught Exception: " + ase.getMessage());
		}
	}
	public static void main(String[] args) throws Exception{
		init();
		try{
			List<AvailabilityZone> zones = ec2.describeAvailabilityZones().getAvailabilityZones();
			System.out.println("You have access to " + zones.size() + " Availability Zones.");
			for (AvailabilityZone zone : zones){
				System.out.println(zone.toString());
			}

			RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
			.withImageId("ami-56e6a404")
			.withInstanceType("t1.micro")
			.withMinCount(1)
			.withMaxCount(1)
			.withKeyName("RM.EC2.Test")
			.withSecurityGroups("RMSecurityGroup");
			ec2.runInstances(runInstancesRequest);
		
			DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
			List<Reservation> reservations = describeInstancesRequest.getReservations();
			Set<Instance> instances = new HashSet<Instance>();
			ArrayList<String> instancesToTerminate = new ArrayList<String>();
			for (Reservation reservation : reservations){
				instances.addAll(reservation.getInstances());
			}
			for (Instance instance : instances){
				String status = instance.getState().getName();
				if (status.equals("terminated")) continue;
				System.out.println("Instance ID : " + instance.getInstanceId() + " ; Instance status : "+status);
				instancesToTerminate.add(instance.getInstanceId());
			}
			System.out.println("----------Terminating----------");
			TerminateInstancesRequest term = new TerminateInstancesRequest();
			term.setInstanceIds(instancesToTerminate);
			ec2.terminateInstances(term);
			for (Instance instance : instances){
				String status = instance.getState().getName();
				if (status.equals("terminated")) continue;
				System.out.println("Instance ID : " + instance.getInstanceId() + " ; Instance status : "+status);
			}
		} catch (AmazonServiceException ase) {
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}
	}
}
