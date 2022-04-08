/*
 * PhysicsParams.h
 *
 *  Created on: 2010/11/29
 *      Author: nakajo
 *  [[ CONFIDENTIAL ]]
 */

#ifndef PHYSICSPARAMS_H_
#define PHYSICSPARAMS_H_

#include "PhysicsHair.h"

//=================================================
//=================================================
class IPhysicsSrc{
public:
	IPhysicsSrc(const char* _paramID , float _scale , float _weight ) ;
	virtual ~IPhysicsSrc();

	virtual void updateSrc(live2d::ALive2DModel * model , PhysicsHair &hair ) ;
//	virtual void updateSrc(live2d::ALive2DModel * model);

protected:
	const char * paramID ;//‰ð•ú‚µ‚È‚¢
	float scale ;
	float weight ;
};

//=================================================
class PhysicsSrc : public IPhysicsSrc {
public:
	PhysicsSrc( PhysicsHair::Src srcType , const char*_paramID , float _scale , float _weight ) ;
	virtual ~PhysicsSrc();

	virtual void updateSrc(live2d::ALive2DModel * model , PhysicsHair &hair ) ;

protected:
	PhysicsHair::Src srcType ;
} ;

//=================================================
//=================================================
class IPhysicsTarget{
public:
	IPhysicsTarget(const char* _paramID , float _scale , float _weight ) ;
	virtual ~IPhysicsTarget();

	virtual void updateTarget(live2d::ALive2DModel * model , PhysicsHair &hair ) ;
//	virtual void updateSrc(live2d::ALive2DModel * model);

protected:
	const char * paramID ;//‰ð•ú‚µ‚È‚¢
	float scale ;
	float weight ;
};

//=================================================
class PhysicsTarget : public IPhysicsTarget {
public:
	PhysicsTarget( PhysicsHair::Target targetType , const char*_paramID , float _scale , float _weight ) ;
	virtual ~PhysicsTarget();

	virtual void updateTarget(live2d::ALive2DModel * model , PhysicsHair &hair ) ;

protected:
	PhysicsHair::Target targetType ;
} ;


#endif /* PHYSICSPARAMS_H_ */
