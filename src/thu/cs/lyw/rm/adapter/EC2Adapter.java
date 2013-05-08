package thu.cs.lyw.rm.adapter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
//import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
//import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
//import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import thu.cs.lyw.rm.evaluation.REvaluation;
import thu.cs.lyw.rm.resource.RNode;
import thu.cs.lyw.rm.util.Provider;

public class EC2Adapter extends RAdapter {
	@Override
	public void initProvider(Provider provider) {
		if (provider.isInit()) return;
		else provider.setInit();
		try{
			AWSCredentials myCredential = new BasicAWSCredentials((String)provider.getProperty("accessKey"), (String)provider.getProperty("secretKey"));
			AmazonEC2 ec2 = new AmazonEC2Client(myCredential);
			ec2.setEndpoint("ec2.ap-southeast-1.amazonaws.com");
//			CreateSecurityGroupRequest createSecurityGroupRequest = 
//					new CreateSecurityGroupRequest();
//			createSecurityGroupRequest.withGroupName("rm-test")
//					.withDescription("Resource Manager Security Group");
//			try{
//				ec2.createSecurityGroup(createSecurityGroupRequest);
//			}catch(AmazonServiceException ase){
//				System.out.println(ase.getMessage());
//			}
//			IpPermission ipPermission = new IpPermission();
//			ipPermission.withIpRanges("166.111.0.0/16", "166.111.0.0/16")
//				.withIpProtocol("tcp")
//				.withFromPort(22)
//				.withToPort(22);
//			AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest =
//					new AuthorizeSecurityGroupIngressRequest();
//			authorizeSecurityGroupIngressRequest.withGroupName("RMSecurityGroup")
//				.withIpPermissions(ipPermission);
//			try{
//				ec2.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);
//			}catch(AmazonServiceException ase){
//				System.out.println(ase.getMessage());
//			}
			CreateKeyPairRequest createKeyPairRequest = 
					new CreateKeyPairRequest();
			createKeyPairRequest.withKeyName("rm-test");
			String privateKey = null;
			try{
				CreateKeyPairResult createKeyPairResult = ec2.createKeyPair(createKeyPairRequest);
				KeyPair keyPair = new KeyPair();
		    	keyPair = createKeyPairResult.getKeyPair();	
				privateKey = keyPair.getKeyMaterial();
			}catch(AmazonServiceException ase){
				System.out.println("EC2 : "+ase.getMessage());
			}
			provider.addProperty("AmazonEC2", ec2);
			provider.addProperty("privateKey", privateKey);
		} catch (AmazonServiceException ase) {
			System.out.println("Caught Exception: " + ase.getMessage());
			System.out.println("Reponse Status Code: " + ase.getStatusCode());
			System.out.println("Error Code: " + ase.getErrorCode());
			System.out.println("Request ID: " + ase.getRequestId());
		}
	}
	@Override
	public RNode getNodeFromProvider(Provider provider, REvaluation evaluation) {
		if (!provider.isInit()) initProvider(provider);
		RNode node = new RNode(provider);
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
		.withImageId(evaluation.image.getImageName())
		.withInstanceType("t1.micro")
		.withMinCount(1)
		.withMaxCount(1)
		.withKeyName(evaluation.task.keyName)
		.withSecurityGroups(evaluation.task.securityGroup);
		AmazonEC2 ec2 = (AmazonEC2)provider.getProperty("AmazonEC2");
		RunInstancesResult runInstancesResult = ec2.runInstances(runInstancesRequest);
		String instanceId = runInstancesResult.getReservation().getInstances().get(0).getInstanceId();
		System.out.println("EC2 : Current state of the newly created instance is : 'pending'.\n" +
				"EC2 : Waiting for it to become 'running'.");
		DescribeInstanceStatusRequest discribeStatusRequest = new DescribeInstanceStatusRequest().withInstanceIds(instanceId);
		DescribeInstanceStatusResult describeStatusResult = ec2.describeInstanceStatus(discribeStatusRequest);
		while (describeStatusResult.getInstanceStatuses().size() == 0){
			describeStatusResult = ec2.describeInstanceStatus(discribeStatusRequest);
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				System.out.println("Who dares to interrupt me?");
				e.printStackTrace();
			}
		}
		DescribeInstancesRequest discribeInstanceRequest = new DescribeInstancesRequest().withInstanceIds(instanceId);
		DescribeInstancesResult describeInstanceStatusResult = ec2.describeInstances(discribeInstanceRequest);
		node.setIP(describeInstanceStatusResult.getReservations().get(0).getInstances().get(0).getPublicIpAddress());
		node.addProperty("instanceId", instanceId);
		System.out.println("EC2 : Public IP of the newly created instance is : " + node.getIP() + ".");
		return node;
	}
	@Override
	public void releaseNodeFromProvider(RNode node) {
		Provider provider = node.getProvider();
		AmazonEC2 ec2 = (AmazonEC2)provider.getProperty("AmazonEC2");
		TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest().
				withInstanceIds((String)node.getProperty("instanceId"));
		ec2.terminateInstances(terminateInstancesRequest);
		System.out.println("EC2 : Public IP of the newly terminated instance is : " + node.getIP() + ".");
	}
}
